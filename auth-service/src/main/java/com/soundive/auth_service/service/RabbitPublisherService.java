package com.soundive.auth_service.service;

import com.soundive.auth_service.dto.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitPublisherService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.user.exchange}")
    private String exchange;

    @Value("${rabbitmq.user.created.routing-key}")
    private String routingKey;

    public void publishUserCreated(UserCreatedEvent event) {
        log.info("Publishing user created event: {}", event);
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}