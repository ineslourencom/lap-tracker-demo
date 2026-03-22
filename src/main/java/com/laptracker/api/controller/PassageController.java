package com.laptracker.api.controller;

import com.laptracker.api.model.request.PassageRecordRequest;
import com.laptracker.event.PassageRecordEvent;
import com.laptracker.event.producer.PassageEventProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/passages")
@RequiredArgsConstructor
public class PassageController {

    private final PassageEventProducer passageEventProducer;

    /**
     * Endpoint to asynchronously record a passage.
     *
     * @param request the request to record a passage
     * @return accepted status
     */
    @PostMapping("/record")
    public ResponseEntity<Void> passageRecord(@Valid @RequestBody PassageRecordRequest request) {
        PassageRecordEvent event = new PassageRecordEvent(request.kartNumber(), request.timestamp());
        CompletableFuture.runAsync(() -> passageEventProducer.publishPassageEvent(event));
        return ResponseEntity.accepted().build();
    }

}
