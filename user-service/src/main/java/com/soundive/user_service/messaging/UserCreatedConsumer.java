package com.soundive.user_service.messaging;

import com.soundive.user_service.entity.User;
import com.soundive.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCreatedConsumer {

    private final UserRepository userRepository;

    @RabbitListener(queues = "${rabbitmq.user.queue}")
    public void consume(UserCreatedEvent event) {
        if (userRepository.existsByEmail(event.getEmail())) {
            System.out.println("User already exists: " + event.getEmail());
            return;
        }

        User user = User.builder()
                .uuid(event.getUuid())
                .email(event.getEmail())
                .encodedPassword(event.getEncodedPassword())
                .firstName(event.getFirstName())
                .lastName(event.getLastName())
                .birthDate(event.getBirthDate())
                .build();

        userRepository.save(user);
        System.out.println("User saved: " + user.getEmail());
    }
}