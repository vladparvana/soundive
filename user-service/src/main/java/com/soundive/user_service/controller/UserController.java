package com.soundive.user_service.controller;

import com.soundive.common.controller.BaseController;
import com.soundive.common.dto.UserValidationRequest;
import com.soundive.common.dto.UserValidationResponse;
import com.soundive.user_service.dto.UserDTO;
import com.soundive.user_service.entity.Role;
import com.soundive.user_service.entity.User;
import com.soundive.user_service.exception.UserNotFoundException;
import com.soundive.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        log.info("Fetching all users");
        List<UserDTO> users = userService.findAll();
        return respondOk(users);
    }


    @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO> getUser(@PathVariable String uuid) {
        log.info("Fetching user with UUID: {}", uuid);
        return userService.findById(uuid)
                .map(this::respondOk)
                .orElseThrow(() -> new UserNotFoundException("User with UUID " + uuid + " not found."));
    }


    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        log.info("Creating new user: {}", userDTO.getEmail());
        UserDTO createdUser = userService.create(userDTO);
        return respondCreated(createdUser);

    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String uuid, @RequestBody UserDTO userDTO) {
        log.info("Updating user with UUID: {}", uuid);
        UserDTO updatedUser = userService.update(uuid, userDTO);
        return respondOk(updatedUser);

    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<UserDTO> patchUser(@PathVariable String uuid, @RequestBody Map<String, Object> updates) {
        log.info("Patching user with UUID: {}", uuid);
        UserDTO patchedUser = userService.patch(uuid, updates);
        return respondOk(patchedUser);
    }

    @PatchMapping("/{uuid}/roles")
    public ResponseEntity<UserDTO> addRoleToUser(@PathVariable String uuid, @RequestBody Role role) {
        log.info("Adding role [{}] to user with UUID: {}", role, uuid);
        UserDTO updatedUser= userService.addRole(uuid, role);
        return respondOk(updatedUser);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable String uuid) {
        log.info("Deleting user with UUID: {}", uuid);
        userService.delete(uuid);
        return respondNoContent();
    }

    @PostMapping("/validate")
    public ResponseEntity<UserValidationResponse> validateUser(@RequestBody UserValidationRequest request) {
        log.info("Validating user request: {}", request);
        return userService.validateUser(request)
                .map(this::respondOk)
                .orElseThrow(() -> new UserNotFoundException("User validation failed"));
    }
}