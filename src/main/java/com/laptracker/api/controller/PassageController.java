package com.laptracker.api.controller;

import com.laptracker.api.model.request.RecordLapRequest;
import com.laptracker.event.LapRecordEvent;
import com.laptracker.event.producer.LapEventProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;


import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/laps")
public class LapController {

    private final LapEventProducer lapEventProducer;

    @Autowired
    public LapController(LapEventProducer lapEventProducer) {
        this.lapEventProducer = lapEventProducer;
    }



    @PostMapping("/record")
    public ResponseEntity<Void> recordLap(@Valid @RequestBody RecordLapRequest request) {
        LapRecordEvent event = new LapRecordEvent(request.kartNumber(), request.timestamp());
        CompletableFuture.runAsync(() -> lapEventProducer.publishPassageEvent(event));
        return ResponseEntity.accepted().build();
    }

}
