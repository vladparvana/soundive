package com.soundive.user_service.messaging;

import com.soundive.user_service.dto.UserDTO;
import com.soundive.user_service.entity.Role;
import com.soundive.user_service.entity.User;
import com.soundive.user_service.messaging.UserCreatedEvent;
import com.soundive.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final UserService userService;

    @RabbitListener(queues = "${rabbitmq.user.queue}")
    @Transactional
    public void consume(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent for email: {}", event.getEmail());

        // Check if the user already exists
        if (userService.getUserByEmail(event.getEmail()).isPresent()) {
            log.warn("User already exists for email: {}", event.getEmail());
            return;
        }

        // Create and save the user
        UserDTO newUser = UserDTO.builder()
                .id(event.getUuid())
                .email(event.getEmail())
                .encodedPassword(event.getEncodedPassword())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .birthDate(event.getBirthDate())
                .roles(Set.of(Role.USER)) // Default to USER role
                .build();

        userService.create(newUser);
        log.info("User created and saved: {}", newUser.getEmail());
    }
}