package com.laptracker.service;

import com.laptracker.api.dto.request.StartRaceRequest;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Race;

import java.util.List;

import com.laptracker.service.domain.KartService;
import com.laptracker.service.domain.LapService;
import com.laptracker.service.domain.RaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RaceApplicationService {

    private final RaceService raceService;
    private final KartService kartService;
    private final LapService lapService;

    @Transactional
    public Race startRace(StartRaceRequest request) {
        Race race = raceService.createRace(request);
        List<Kart> karts = kartService.createKartsForRace(race.getId(), request.kartNumbers());
        karts.forEach(kart -> lapService.createInitialLap(kart.getId(), race.getStartedAt()));
        return race;
    }
}
