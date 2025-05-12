package com.soundive.auth_service.service;

import com.soundive.auth_service.dto.RefreshTokenDTO;
import com.soundive.auth_service.entity.RefreshToken;
import com.soundive.auth_service.exception.TokenRefreshException;
import com.soundive.auth_service.exception.TokenValidationException;
import com.soundive.auth_service.mapper.RefreshTokenMapper;
import com.soundive.auth_service.repository.RefreshTokenRepository;
import com.soundive.common.entity.UuidIdStrategy;
import com.soundive.common.service.impl.BaseServiceImpl;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class RefreshTokenService extends BaseServiceImpl<RefreshToken, RefreshTokenDTO,String> {
    private final RefreshTokenRepository repository;
    private final RefreshTokenMapper mapper;



    @Autowired
    public RefreshTokenService(RefreshTokenRepository repository, RefreshTokenMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
        this.mapper = mapper;
    }

    public RefreshTokenDTO create(RefreshTokenDTO refreshToken) {

        repository.deleteByUserEmail(refreshToken.getUserEmail());

        return mapper.toDto(repository.save(mapper.toEntity(refreshToken)));
    }

    public Optional<RefreshTokenDTO> findByToken(String token) {
        return Optional.ofNullable(repository.findByToken(token).map(mapper::toDto).orElseThrow(() -> new TokenValidationException("Invalid token")));
    }

    public RefreshTokenDTO verifyExpiration(RefreshTokenDTO refreshTokenDTO) {
        RefreshToken refreshToken = mapper.toEntity(refreshTokenDTO);
        if (refreshToken.isExpired() || refreshToken.isRevoked()) {
            repository.delete(refreshToken);
            throw new TokenRefreshException(refreshTokenDTO.getToken() + "Refresh token was expired or revoked. Please sign in again");
        }
        return mapper.toDto(refreshToken);
    }

    @Transactional
    public void revokeRefreshToken(String userEmail) {
        repository.findByUserEmail(userEmail)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    repository.save(token);
                });
    }




}
