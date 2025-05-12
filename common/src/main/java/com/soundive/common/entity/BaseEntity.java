package com.soundive.common.entity;

import com.soundive.common.util.IdGeneratorFactory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Enhanced Base Entity with flexible ID generation.
 */
@Getter
@Setter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    private String id ;



    protected BaseEntity(BaseIdStrategy idStrategy) {
        this.id = idStrategy.generateId().toString();
    }

    // Default constructor for JPA
    protected BaseEntity() {
    }

}