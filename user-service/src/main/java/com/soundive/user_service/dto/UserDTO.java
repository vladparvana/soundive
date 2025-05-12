package com.soundive.user_service.dto;

import com.soundive.common.annotation.ExcludeFromDto;
import com.soundive.common.dto.AuditableDto;
import com.soundive.common.dto.BaseDto;
import com.soundive.user_service.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

/**
 * Data Transfer Object for User Entity.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder(toBuilder = true)
@Getter
@Setter
public class UserDTO extends AuditableDto {
    @NotBlank(message = "Email must not be blank.")
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private LocalDate birthDate;
    @NotBlank
    @ExcludeFromDto
    private String encodedPassword;
    private Set<Role> roles;

    public UserDTO() {
        super();
    }
}