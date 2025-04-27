package com.soundive.user_service.controller;

import com.soundive.user_service.dto.UserValidationRequest;
import com.soundive.user_service.dto.UserValidationResponse;
import com.soundive.user_service.entity.User;
import com.soundive.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/validate")
    public ResponseEntity<UserValidationResponse> validateUser(@RequestBody UserValidationRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> ResponseEntity.ok(new UserValidationResponse(user.getEncodedPassword())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}
