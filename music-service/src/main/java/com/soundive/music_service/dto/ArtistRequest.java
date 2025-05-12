package com.soundive.music_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistRequest {

    @NotBlank(message = "Stage name is required")
    private String stageName;

    @NotBlank(message = "User ID is required")
    private String userId;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    private String description;
}