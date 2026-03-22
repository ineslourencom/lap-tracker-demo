package com.laptracker.event.producer;

import com.laptracker.config.RabbitMQConfig;
import com.laptracker.event.LapRecordEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class LapEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public LapEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPassageEvent(LapRecordEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event);
    }
}
