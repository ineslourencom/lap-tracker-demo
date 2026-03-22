package com.laptracker.service.domain;

import com.laptracker.persistence.RaceRepository;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Race;
import com.laptracker.persistence.entity.RaceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RaceService {

    private final RaceRepository raceRepository;


    /**
     * Creates a new race with the given parameters.
     * @param name The name of the race.
     * @param laps The total number of laps.
     * @param kartNumbers A list of kart numbers participating in the race.
     * @return The created race.
     */
    @Transactional
    public Race createRace(String name, Integer laps, List<Integer> kartNumbers) {
        Race race = new Race(name, laps, RaceStatus.STARTED, LocalDateTime.now());
        for (Integer num : kartNumbers) {
             Kart kart = new Kart(num, race);
             race.getKarts().add(kart);
        }
        return raceRepository.save(race);
    }


    /**
     * Retrieves the currently active race.
     * @return The active race.
     * @throws IllegalStateException if no active race is found.
     */
    @Transactional(readOnly = true)
    public Race getActiveRace() {
        return raceRepository.findFirstByStatus(RaceStatus.STARTED)
                .orElseThrow(() -> new IllegalStateException("No active race found"));
    }


    /**
     * Finds a race by its ID.
     * @param raceId The ID of the race to find.
     * @return The found race.
     * @throws IllegalArgumentException if the race is not found.
     */
    @Transactional(readOnly = true)
    public Race findById(UUID raceId) {
        return raceRepository.findById(raceId)
                .orElseThrow(() -> new IllegalArgumentException("Race not found"));
    }


    /**
     * Marks the race as finished.
     * @param raceId The ID of the race to finish.
     */
    @Transactional
    public void finishRace(UUID raceId) {
        Race race = findById(raceId);
        race.setStatus(RaceStatus.FINISHED);
        raceRepository.save(race);
    }
}
