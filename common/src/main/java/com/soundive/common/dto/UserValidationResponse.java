package com.soundive.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserValidationResponse implements MarkerDto
{
    private String encodedPassword;
}