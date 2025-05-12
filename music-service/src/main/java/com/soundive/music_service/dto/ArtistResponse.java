package com.soundive.music_service.dto;


import lombok.Data;

@Data
public class ArtistResponse {
    private String id;
    private String stageName;
    private String email;
    private String description;
}