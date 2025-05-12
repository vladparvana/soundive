package com.soundive.music_service.mapper;

import com.soundive.music_service.dto.ArtistRequest;
import com.soundive.music_service.dto.ArtistResponse;
import com.soundive.music_service.model.Artist;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

    public Artist toEntity(ArtistRequest request) {
        return Artist.builder()
                .stageName(request.getStageName())
                .userId(request.getUserId())
                .email(request.getEmail())
                .description(request.getDescription())
                .build();
    }

    public ArtistResponse toDto(Artist artist) {
        ArtistResponse dto = new ArtistResponse();
        dto.setId(artist.getId());
        dto.setStageName(artist.getStageName());
        dto.setEmail(artist.getEmail());
        dto.setDescription(artist.getDescription());
        return dto;
    }
}
