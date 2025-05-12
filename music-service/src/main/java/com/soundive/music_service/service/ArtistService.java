package com.soundive.music_service.service;

import com.soundive.music_service.dto.ArtistRequest;
import com.soundive.music_service.dto.ArtistResponse;
import com.soundive.music_service.mapper.ArtistMapper;
import com.soundive.music_service.model.Artist;
import com.soundive.music_service.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    public ArtistResponse createArtist(ArtistRequest request) {
        Artist artist = artistMapper.toEntity(request);
        Artist saved = artistRepository.save(artist);
        return artistMapper.toDto(saved);
    }

    public Optional<ArtistResponse> getByUserId(String userId) {
        return artistRepository.findByUserId(userId)
                .map(artistMapper::toDto);
    }
}
