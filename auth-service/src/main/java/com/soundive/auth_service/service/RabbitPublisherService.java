package com.soundive.auth_service.service;

import com.soundive.auth_service.dto.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitPublisherService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.user.exchange}")
    private String exchange;

    @Value("${rabbitmq.user.created.routing-key}")
    private String routingKey;

    public void publishUserCreated(UserCreatedEvent event) {
        rabbitTemplate.convertAndSend(exchange, routingKey, event);
    }
}