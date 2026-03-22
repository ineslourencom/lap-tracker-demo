package com.laptracker.event.consumer;

import com.laptracker.service.PassageApplicationService;
import com.laptracker.config.RabbitMQConfig;
import com.laptracker.event.PassageRecordEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LapEventConsumer {

    private final PassageApplicationService passageApplicationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handlePassageEvent(PassageRecordEvent event) {
        log.info("Received passage event: {}", event);
        passageApplicationService.recordPassage(event.kartNumber(), event.timestamp());
    }
}
