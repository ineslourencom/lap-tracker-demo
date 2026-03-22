package com.laptracker.api.controller;

import com.laptracker.api.model.request.StartRaceRequest;
import com.laptracker.api.model.response.RaceResultResponse;
import com.laptracker.service.RaceApplicationService;
import com.laptracker.service.RaceResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/races")
@RequiredArgsConstructor
public class RaceController {

    private final RaceApplicationService raceApplicationService;
    private final RaceResultService raceResultService;


    @PostMapping
    public ResponseEntity<String> startRace(@Valid @RequestBody StartRaceRequest request) {
        var raceId = raceApplicationService.startRace(request);
        return ResponseEntity.ok("Race has been started with id " + raceId.toString());
    }

    /**
     * Endpoint to retrieve the active race ID.
     *
     * @return the id of the active race
     */
    @GetMapping("/active")
    public ResponseEntity<String> getActiveRace() {
        var raceId = raceApplicationService.getActiveRaceId();
        return ResponseEntity.ok(raceId.toString());
    }

    /**
     * Endpoint to finish a race.
     *
     * @param raceId the id of the race to finish
     * @return the results of the race
     */
    @PutMapping("/{raceId}/finish")
    public ResponseEntity<RaceResultResponse> finishRace(@PathVariable UUID raceId) {
        log.info("Race with id {} requested to finish", raceId);
        return ResponseEntity.ok(raceApplicationService.finishRace(raceId));
    }

    /**
     * Endpoint to retrieve race results.
     *
     * @param raceId the id of the race
     * @return the results of the race
     */
    @GetMapping("/{raceId}/results")
    public ResponseEntity<RaceResultResponse> getResults(@PathVariable UUID raceId) {
        return ResponseEntity.ok(raceResultService.getRaceResult(raceId));
    }
}
