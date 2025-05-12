package com.soundive.user_service.messaging;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Event DTO for publishing user creation details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreatedEvent {

    @NotBlank(message = "UUID must not be blank.")
    private String uuid;

    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email is mandatory.")
    private String email;

    @NotBlank(message = "Encoded password is mandatory.")
    private String encodedPassword;

    @NotBlank(message = "First name is mandatory.")
    private String firstName;

    @NotBlank(message = "Last name is mandatory.")
    private String lastName;

    @NotNull(message = "Birthdate is mandatory.")
    private LocalDate birthDate;
}