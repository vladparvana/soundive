package com.soundive.auth_service.service;

import com.soundive.auth_service.entity.RefreshToken;
import com.soundive.auth_service.exception.TokenRefreshException;
import com.soundive.common.client.UserServiceFeignClient;
import com.soundive.auth_service.dto.*;
import com.soundive.auth_service.dto.UserCreatedEvent;
import com.soundive.auth_service.exception.AuthenticationException;
import com.soundive.auth_service.security.JwtService;
import com.soundive.common.dto.UserValidationRequest;
import com.soundive.common.dto.UserValidationResponse;
import com.soundive.common.entity.UuidIdStrategy;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RabbitPublisherService rabbitPublisher;
    private final RestTemplate restTemplate;
    private final RefreshTokenService refreshTokenService;
    private final UserServiceFeignClient userServiceClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "registerFallback")
    public ResponseEntity<Void> register(RegisterRequest request) {
        log.info("Registering user with email: {}", request.getEmail());
        try {
            UserValidationResponse validationResponse = userServiceClient.validateUser(new UserValidationRequest(request.getEmail())).getBody();

            if (validationResponse != null) {
                log.warn("User with email {} already exists.", request.getEmail());
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

        }
        catch (FeignException ex)
        {
            if (ex.status() == HttpStatus.NOT_FOUND.value()) {
                log.info("User with email {} does not exist. Proceeding with registration.", request.getEmail());
                String uuid = new UuidIdStrategy().generateId().toString();
                String encodedPassword = passwordEncoder.encode(request.getPassword());

                UserCreatedEvent event = UserCreatedEvent.builder()
                        .uuid(uuid)
                        .email(request.getEmail())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .birthDate(request.getBirthDate())
                        .encodedPassword(encodedPassword)
                        .build();

                rabbitPublisher.publishUserCreated(event);
                return ResponseEntity.noContent().build();
            }
        } catch (Exception ex) {
            log.error("Error during user registration for email {}: {}", request.getEmail(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        log.warn("User with email {} already exists.", request.getEmail());
        return ResponseEntity.status(HttpStatus.CONFLICT).build();

    }


    public ResponseEntity<Void> registerFallback(RegisterRequest request, Throwable ex) {
        log.error("Fallback triggered for register() with email [{}]: {}", request.getEmail(), ex.toString());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
    }

    @CircuitBreaker(name = "userService", fallbackMethod = "loginFallback")
    public TokenResponse login(AuthRequest request) {
        log.info("Attempting login for email: {}", request.getEmail());
        try {
            UserValidationResponse validationResponse = userServiceClient.validateUser(new UserValidationRequest(request.getEmail())).getBody();

            if (validationResponse == null) {
                log.warn("Authentication failed for email: {}", request.getEmail());
                throw new AuthenticationException("Login failed for email: " + request.getEmail());
            }

            if (!passwordEncoder.matches(request.getPassword(), validationResponse.getEncodedPassword())) {
                log.warn("Invalid credentials provided for email: {}", request.getEmail());
                throw new AuthenticationException("Invalid credentials provided for email: " + request.getEmail());
            }

            String accessToken = jwtService.generateAccessToken(request.getEmail());
            String refreshToken = jwtService.generateRefreshToken(request.getEmail());
            RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                    .token(refreshToken)
                    .expiryDate(jwtService.getTokenExpirationDate(refreshToken).toInstant())
                    .userEmail(request.getEmail())
                    .revoked(false)
                    .build();

            RefreshTokenDTO newRefreshToken = refreshTokenService.create(refreshTokenDTO);



            TokenResponse tokenResponse=  TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshTokenDTO.getToken())
                    .accessTokenExpiresIn(jwtService.getTokenExpirationDate(accessToken).toInstant().toEpochMilli() - System.currentTimeMillis())
                    .refreshTokenExpiresIn(jwtService.getTokenExpirationDate(refreshToken).toInstant().toEpochMilli() - System.currentTimeMillis())
                    .build();

            log.info("Authentication successful for email: {}", request.getEmail());
            return tokenResponse;
        } catch (Exception ex) {
            log.error("Error during login for email {}: {}", request.getEmail(), ex.getMessage());
            throw new AuthenticationException("Login failed for email: " + request.getEmail());
        }
    }

    public TokenResponse refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshTokenDTO::getUserEmail)
                .map(userEmail -> {
                    String accessToken = jwtService.generateAccessToken(userEmail);
                    String refreshTokenNew = jwtService.generateRefreshToken(userEmail);
                    RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                            .token(refreshToken)
                            .expiryDate(jwtService.getTokenExpirationDate(refreshToken).toInstant())
                            .userEmail(userEmail)
                            .revoked(false)
                            .build();
                    RefreshTokenDTO newRefreshToken = refreshTokenService.create(refreshTokenDTO);

                    return   TokenResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshTokenDTO.getToken())
                            .accessTokenExpiresIn(jwtService.getTokenExpirationDate(accessToken).toInstant().toEpochMilli() - System.currentTimeMillis())
                            .refreshTokenExpiresIn(jwtService.getTokenExpirationDate(refreshToken).toInstant().toEpochMilli() - System.currentTimeMillis())
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken +  " Refresh token not found"));
    }

    public void logout(String userEmail) {
        refreshTokenService.revokeRefreshToken(userEmail);
    }


    public TokenResponse loginFallback(AuthRequest request, Throwable ex) {
        log.error("Fallback triggered for login() with email [{}]: {}", request.getEmail(), ex.toString());
        throw new AuthenticationException("Login failed for email: " + request.getEmail());
    }


}