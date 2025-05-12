package com.soundive.auth_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for authentication response containing JWT token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @NotNull(message = "Token must not be null.")
    private String token;

    @Override
    public String toString() {
        return "AuthResponse{" +
                "token='********'" +
                '}';
    }
}