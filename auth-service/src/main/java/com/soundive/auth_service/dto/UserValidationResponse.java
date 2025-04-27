package com.soundive.auth_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationResponse {
    private String encodedPassword;
}