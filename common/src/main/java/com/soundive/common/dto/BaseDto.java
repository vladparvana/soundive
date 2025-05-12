package com.soundive.common.dto;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Base class for all DTOs that have an ID.
 */
@Data
@Getter
@Setter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public abstract class BaseDto implements MarkerDto {
    @NotBlank(message = "ID is required")
    private String id;

    protected BaseDto() {}


}