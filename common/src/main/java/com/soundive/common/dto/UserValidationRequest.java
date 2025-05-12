package com.soundive.common.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationRequest implements  MarkerDto{
    private String email;
}