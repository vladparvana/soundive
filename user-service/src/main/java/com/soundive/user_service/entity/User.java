package com.soundive.user_service.entity;

import com.soundive.common.annotation.ExcludeFromDto;
import com.soundive.common.entity.AuditableEntity;
import com.soundive.common.entity.BaseIdStrategy;
import com.soundive.common.entity.UuidIdStrategy;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class User extends AuditableEntity {
    @Email(message = "Email must be a valid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @ExcludeFromDto
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String encodedPassword;

    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    @NotNull(message = "Birthdate is required")
    @ExcludeFromDto
    private LocalDate birthDate; // Optional field

    @Column(nullable = false)
    private boolean enabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_uuid"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles; // User roles

    public User() {
        super(new UuidIdStrategy());
    }

    public void addRole(Role role) {
        roles.add(role);
    }


    protected BaseIdStrategy getStrategy() {
        return new UuidIdStrategy();
    }



}