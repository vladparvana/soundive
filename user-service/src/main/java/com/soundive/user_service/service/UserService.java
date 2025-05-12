package com.soundive.user_service.service;

import com.soundive.common.dto.UserValidationRequest;
import com.soundive.common.dto.UserValidationResponse;
import com.soundive.common.service.impl.BaseServiceImpl;
import com.soundive.user_service.dto.UserDTO;
import com.soundive.user_service.entity.Role;
import com.soundive.user_service.entity.User;
import com.soundive.user_service.exception.UserAlreadyExistsException;
import com.soundive.user_service.exception.UserNotFoundException;
import com.soundive.user_service.mapper.UserMapper;
import com.soundive.user_service.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class UserService extends BaseServiceImpl<User,UserDTO, String> {


    private final UserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper) {
        super(repository,mapper);
        this.repository = repository;
        this.mapper = mapper;
    }


    @Cacheable(value = "userByEmail", key = "#email")
    public Optional<UserDTO> getUserByEmail(String email) {
        log.info("Fetching user by email: {}", email);
        log.info(repository.findByEmail(email).isPresent() ? "User found" : "User not found");
        return repository.findByEmail(email).map(mapper::toDto);
    }


    @Override
    public UserDTO create(@Valid UserDTO userDTO) {
        log.info("Creating user with email: {}", userDTO.getEmail());
        if (repository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("A user with the email " + userDTO.getEmail() + " already exists.");
        }
        User user = mapper.toEntity(userDTO);
        user.setEnabled(Optional.of(user.isEnabled()).orElse(true)); // Default enabled true if null
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }
        return mapper.toDto(repository.save(user));
    }

    @Override
    public UserDTO update(String uuid, @Valid UserDTO updatedUser) {
        log.info("Updating user with UUID: {}", uuid);
        User existingUser = repository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException("User with UUID " + uuid + " not found."));

        if (!existingUser.getEmail().equalsIgnoreCase(updatedUser.getEmail())) {
            throw new IllegalArgumentException("Email cannot be updated.");
        }

        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setBirthDate(updatedUser.getBirthDate());
        existingUser.setEnabled(updatedUser.isEnabled());
        existingUser.setRoles(updatedUser.getRoles());

        return mapper.toDto(repository.save(existingUser));
    }

    public UserDTO patch(String uuid, Map<String, Object> updates) {
        log.info("Patching user with UUID: {}", uuid);
        User user = repository.findById(uuid)
        .orElseThrow(() -> new UserNotFoundException("User with UUID " + uuid + " not found."));

        updates.forEach((field, value) -> {
            switch (field) {
                case "firstName" -> user.setFirstName((String) value);
                case "lastName" -> user.setLastName((String) value);
                case "birthDate" -> user.setBirthDate(LocalDate.parse((String) value));
                case "enabled" -> user.setEnabled(Boolean.parseBoolean(value.toString()));
                default -> throw new IllegalArgumentException("Field update not allowed: " + field);
            }
        });

        return mapper.toDto(repository.save(user));
    }

    @CacheEvict(value = "userCache", key = "#email")
    @Override
    public void delete(String uuid) {
        log.info("Deleting user with UUID: {}", uuid);
        if (!repository.existsById(uuid)) {
            throw new UserNotFoundException("User with UUID " + uuid + " not found.");
        }
        repository.softDelete(repository.findById(uuid).get());
    }

    @Override
    public Optional<UserDTO> findById(String uuid) {
        log.info("Fetching user by UUID: {}", uuid);
        return Optional.ofNullable((repository.findById(uuid).map(mapper::toDto))
                .orElseThrow(() -> new UserNotFoundException("User with UUID " + uuid + " not found.")));
    }

    @Override
    public List<UserDTO> findAll() {
        log.info("Fetching all users");
        return mapper.toDtoList(repository.findAllByNotDeleted(Pageable.unpaged()).getContent());
    }

    public UserDTO addRole(String uuid, Role newRole) {
        log.info("Adding role [{}] to user with UUID: {}", newRole, uuid);
        User user = repository.findById(uuid).get();

        // Ensure no duplicate roles
        if (user.getRoles() != null && user.getRoles().contains(newRole)) {
            throw new IllegalStateException("User already has the role: " + newRole);
        }

        // Add role
        Set<Role> roles = user.getRoles() == null ? new HashSet<>() : user.getRoles();
        roles.add(newRole);
        user.setRoles(roles);

        return mapper.toDto(repository.save(user));
    }

    public Optional<UserValidationResponse> validateUser(UserValidationRequest request) {
        log.info("Validating user with email: {}", request.getEmail());

        return repository.findByEmail(request.getEmail())
                .map(user -> new UserValidationResponse(user.getEncodedPassword()))
                .or(() -> {
                    log.error("User with email {} not found.", request.getEmail());
                    throw new UserNotFoundException("User with email " + request.getEmail() + " not found.");
                });
    }
}