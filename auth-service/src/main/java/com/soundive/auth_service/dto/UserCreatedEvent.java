package com.soundive.auth_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreatedEvent {
    private String uuid;
    private String email;
    private String encodedPassword;
    private String firstName;
    private String lastName;
    private String birthDate;
}