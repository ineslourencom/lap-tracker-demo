package com.laptracker.service;

import com.laptracker.api.model.request.StartRaceRequest;
import com.laptracker.api.model.response.RaceResultResponse;
import com.laptracker.service.domain.RaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RaceApplicationService {

    private final RaceService raceService;
    private final RaceResultService resultService;

    /**
     * Starts a new race.
     *
     * @param request the request to start a race
     * @return the id of the new race
     */
    @Transactional
    public UUID startRace(StartRaceRequest request) {
        return raceService.createRace(request.name(), request.totalLaps(), request.kartNumbers()).getId();
    }

    /**
     * Retrieves the ID of the active race.
     *
     * @return the id of the active race
     */
    @Transactional
    public UUID getActiveRaceId() {
        return raceService.getActiveRace().getId();
    }

    /**
     * Finishes the race and returns the results.
     *
     * @param raceId the id of the race to finish
     * @return the results of the race
     */
    public RaceResultResponse finishRace(UUID raceId) {
        raceService.finishRace(raceId);
        return resultService.getRaceResult(raceId);
    }
}
