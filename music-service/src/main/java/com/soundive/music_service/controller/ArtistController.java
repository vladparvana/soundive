package com.soundive.music_service.controller;

import com.soundive.music_service.dto.ArtistRequest;
import com.soundive.music_service.dto.ArtistResponse;
import com.soundive.music_service.service.ArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping
    public ResponseEntity<ArtistResponse> create(@RequestBody @Valid ArtistRequest request) {
        ArtistResponse response = artistService.createArtist(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ArtistResponse> getByUserId(@PathVariable String userId) {
        return artistService.getByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
