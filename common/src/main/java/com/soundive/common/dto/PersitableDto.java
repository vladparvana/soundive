package com.soundive.common.dto;

import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
public abstract class PersitableDto extends BaseDto {

    @Builder.Default
    private boolean enabled = true;


    protected PersitableDto() {}

}
