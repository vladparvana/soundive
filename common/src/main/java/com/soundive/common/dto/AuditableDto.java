package com.soundive.common.dto;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public abstract class AuditableDto extends PersitableDto {
    private Instant createdAt;
    private Instant updatedAt;


    // Default constructor for JPA
    protected AuditableDto() {}
}
