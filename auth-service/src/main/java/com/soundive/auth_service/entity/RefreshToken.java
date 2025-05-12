package com.soundive.auth_service.entity;

import com.soundive.common.entity.AuditableEntity;
import com.soundive.common.entity.BaseIdStrategy;
import com.soundive.common.entity.UuidIdStrategy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class RefreshToken extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean revoked;

    public RefreshToken() {
        super(new UuidIdStrategy());
    }


    public boolean isExpired() {
        return expiryDate.isBefore(Instant.now());
    }

}
