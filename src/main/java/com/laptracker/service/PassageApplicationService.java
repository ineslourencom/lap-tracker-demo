package com.laptracker.service;

import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.domain.KartService;
import com.laptracker.service.domain.PassageService;
import com.laptracker.service.domain.RaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassageApplicationService {

    private final RaceService raceService;
    private final KartService kartService;
    private final PassageService passageService;


    /**
     * Records a passage for a kart.
     *
     * @param kartNumber the kart number
     * @param timestamp  the timestamp of the passage
     */
    @Transactional
    public void recordPassage(Integer kartNumber, LocalDateTime timestamp) {
        Race activeRace = raceService.getActiveRace();

        Kart kart = kartService.findKartByNumberAndRaceId(kartNumber, activeRace.getId())
                .orElseThrow(() -> new IllegalArgumentException("Kart " + kartNumber + " not found in race " + activeRace.getId()));

        passageService.recordPassage(kart, activeRace, timestamp);
        log.info("R");
    }
}
