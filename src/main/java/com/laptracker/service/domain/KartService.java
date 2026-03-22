package com.laptracker.service.domain;


import com.laptracker.persistence.KartRepository;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Race;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KartService {

    private final KartRepository kartRepository;

    public Kart createKart(Integer kartNumber, Race race) {
        Kart kart = new Kart(kartNumber, race);
        return kartRepository.save(kart);
    }

    public Optional<Kart> findKartByNumberAndRaceId(Integer kartNumber, UUID raceId) {
        return kartRepository.findByKartNumberAndRaceId(kartNumber, raceId);
    }

}
