package com.soundive.auth_service.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenValidationRequest(@NotBlank String token,@NotBlank String email) {}
