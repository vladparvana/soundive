package com.soundive.auth_service.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationRequest {
    private String email;
}