package com.soundive.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * DTO for user registration request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    @NotBlank(message = "First name is mandatory.")
    private String firstName;

    @NotBlank(message = "Last name is mandatory.")
    private String lastName;

    @NotNull(message = "Birthdate is mandatory.")
    @Past(message = "Birthdate must be in the past.")
    private LocalDate birthDate;

    @NotBlank(message = "Password is mandatory.")
    private String password;

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate.format(DateTimeFormatter.ISO_DATE) +
                '}';
    }
}