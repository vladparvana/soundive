package com.soundive.auth_service.controller;

import com.soundive.auth_service.dto.EmailResponse;
import com.soundive.auth_service.dto.TokenRequest;
import com.soundive.auth_service.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthUtilityController {

    private final JwtService jwtService;

    @PostMapping("/extract-email")
    public ResponseEntity<EmailResponse> extractEmail(@RequestBody @Valid TokenRequest request) {
        log.info("Request to extract email from token received");
        try {
            String email = jwtService.extractSubject(request.token());
            log.info("Extracted email: {}", email);
            return ResponseEntity.ok(new EmailResponse(email));
        } catch (IllegalArgumentException e) {
            log.warn("Unauthorized token access: {}", e.getMessage());
            return ResponseEntity.status(401).build();
        }
    }
}