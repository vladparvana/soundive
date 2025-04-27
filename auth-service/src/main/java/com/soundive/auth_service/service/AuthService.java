package com.soundive.auth_service.service;

import com.soundive.auth_service.dto.*;
import com.soundive.auth_service.dto.UserCreatedEvent;
import com.soundive.auth_service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RabbitPublisherService rabbitPublisher;
    private final RestTemplate restTemplate;

    private final String userServiceValidateUrl = "http://localhost:8083/users/validate";

    public ResponseEntity<Void> register(RegisterRequest request) {
        try {
            ResponseEntity<UserValidationResponse> response = restTemplate.postForEntity(
                    userServiceValidateUrl,
                    new UserValidationRequest(request.getEmail()),
                    UserValidationResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                // User EXISTĂ → Conflict 409
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

        } catch (HttpClientErrorException.NotFound ex) {
            // Aici înseamnă că userul NU există => publicăm în RabbitMQ
            String uuid = UUID.randomUUID().toString();
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
        } catch (Exception ex) {
            // Alte erori (ex: server down) -> Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // fallback teoretic
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public ResponseEntity<AuthResponse> login(AuthRequest request) {
        // Verificăm credentialele
        ResponseEntity<UserValidationResponse> response = restTemplate.postForEntity(
                userServiceValidateUrl,
                new UserValidationRequest(request.getEmail()),
                UserValidationResponse.class
        );

        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserValidationResponse user = response.getBody();

        if (!passwordEncoder.matches(request.getPassword(), user.getEncodedPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = jwtService.generateToken(request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}