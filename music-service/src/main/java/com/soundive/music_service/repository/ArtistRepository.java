package com.soundive.music_service.repository;

import com.soundive.music_service.model.Artist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ArtistRepository extends MongoRepository<Artist, String> {
    Optional<Artist> findByUserId(String userId);
}