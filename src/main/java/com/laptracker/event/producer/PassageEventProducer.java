package com.laptracker.event.producer;

import com.laptracker.config.RabbitMQConfig;
import com.laptracker.event.PassageRecordEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Slf4j
@Component
@RequiredArgsConstructor
public class PassageEventProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publishes a passage event to the message queue.
     *
     * @param event The passage event to publish.
     */
    public void publishPassageEvent(PassageRecordEvent event) {
        log.info("About to publish {} of kart {}", event.getClass().getName(), event.kartNumber());
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event);
    }
}
