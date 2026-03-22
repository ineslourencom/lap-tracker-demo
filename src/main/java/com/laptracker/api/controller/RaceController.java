package com.laptracker.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.laptracker.api.dto.request.StartRaceRequest;
import com.laptracker.api.dto.response.RaceResponse;
import com.laptracker.api.dto.response.RaceResultResponse;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.LapService;
import com.laptracker.service.RaceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/races")
@RequiredArgsConstructor
public class RaceController {

    private final RaceService raceService;
    private final LapService lapService;

    @PostMapping
    public ResponseEntity<Race> startRace(@RequestBody StartRaceRequest request) {
        var result = raceService.startRace(request.name(), request.kartNumbers());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/finish")
    public ResponseEntity<RaceResultResponse> finishRace(@PathVariable Long id) {
        RaceResultResponse result = raceService.finishRace(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/results")
    public ResponseEntity<List<RaceResultResponse>> getRaceResults(@PathVariable Long id) {
        return ResponseEntity.ok(lapService.getRaceResults(id));
    }
}
