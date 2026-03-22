package com.laptracker.service;

import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.domain.KartService;
import com.laptracker.service.domain.PassageService;
import com.laptracker.service.domain.RaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

@Service
@RequiredArgsConstructor
public class LapRecordApplicationService {

    private final RaceService raceService;
    private final KartService kartService;
    private final PassageService passageService;


    @Transactional
    public void recordLap(Integer kartNumber, LocalDateTime timestamp) {
        Race activeRace = raceService.getActiveRace();

        Kart kart = kartService.findKartByNumberAndRaceId(kartNumber, activeRace.getId())
                .orElseThrow(() -> new IllegalArgumentException("Kart " + kartNumber + " not found in race " + activeRace.getId()));

        passageService.recordPassage(kart, activeRace, timestamp);
    }
}
