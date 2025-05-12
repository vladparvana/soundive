package com.soundive.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for user authentication request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    @NotBlank(message = "Password is mandatory.")
    private String password;

    @Override
    public String toString() {
        return "AuthRequest{" +
                "email='" + email + '\'' +
                '}';
    }
}