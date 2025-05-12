package com.soundive.auth_service.dto;

import com.soundive.common.dto.AuditableDto;
import com.soundive.common.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Getter
@Setter
public class RefreshTokenDTO extends AuditableDto {
    private String token;
    private String userEmail;
    public boolean revoked;
    private Instant expiryDate;

    public RefreshTokenDTO() {
        super();
    }
}
