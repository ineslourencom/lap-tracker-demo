package com.laptracker.service;

import com.laptracker.api.dto.request.StartRaceRequest;
import com.laptracker.persistence.RaceRepository;
import com.laptracker.persistence.entity.Race;
import com.laptracker.persistence.entity.RaceStatus;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RaceService {

    private final RaceRepository raceRepository;

    @Transactional
    public Race createRace(StartRaceRequest request) {
        Race race = new Race();
        race.setName(request.name());
        race.setTotalLaps(request.totalLaps());
        race.setStartedAt(LocalDateTime.now());
        race.setStatus(RaceStatus.STARTED);
        return raceRepository.save(race);
    }

    @Transactional(readOnly = true)
    public Race getActiveRace() {
        return raceRepository.findFirstByStatusOrderByStartedAtDesc(RaceStatus.STARTED)
                .orElseThrow(() -> new IllegalStateException("No active race found"));
    }

    @Transactional(readOnly = true)
    public Race findById(Long raceId) {
        return raceRepository.findById(raceId)
                .orElseThrow(() -> new IllegalArgumentException("Race not found"));
    }

    @Transactional
    public Race finishRace(Long raceId) {
        Race race = findById(raceId);
        race.setStatus(RaceStatus.FINISHED);
        return raceRepository.save(race);
    }
}
