package com.soundive.auth_service.controller;

import com.soundive.auth_service.dto.*;
import com.soundive.auth_service.exception.TokenValidationException;
import com.soundive.auth_service.security.JwtService;
import com.soundive.auth_service.service.AuthService;
import com.soundive.common.controller.BaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Received register request for email: {}", request.getEmail());
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AuthRequest request) {
        log.info("Received login request for email: {}", request.getEmail());
        return respondOk(authService.login(request));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Void> validateToken(@Valid @RequestBody TokenValidationRequest request) {
        log.info("Received token validation request for email: {}", request.email());
        try {
            boolean isValid = jwtService.isValid(request.token(), request.email());
            log.info("Token validation for email: {} - valid: {}", request.email(), isValid);
            return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Token validation failed for email: {}", request.email(), e);
            throw new TokenValidationException("Invalid token for email: " + request.email());
        }
    }
}