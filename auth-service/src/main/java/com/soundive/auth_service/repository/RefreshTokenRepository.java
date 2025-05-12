package com.soundive.auth_service.repository;

import com.soundive.auth_service.entity.RefreshToken;
import com.soundive.common.repository.AuditableRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends AuditableRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserEmail(String userEmail);
    void deleteByUserEmail(String userEmail);
}

