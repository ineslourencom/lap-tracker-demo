package com.laptracker.event.consumer;

import com.laptracker.config.RabbitMQConfig;
import com.laptracker.event.LapPassageEvent;
import com.laptracker.service.PassageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LapEventConsumer {

    private final PassageService passageService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handlePassageEvent(LapPassageEvent event) {
        log.info("Received passage event: {}", event);
        passageService.recordPassage(event.kartNumber(), event.timestamp());
    }
}
