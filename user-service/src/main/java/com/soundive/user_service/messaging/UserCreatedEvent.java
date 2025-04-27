package com.soundive.user_service.messaging;

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