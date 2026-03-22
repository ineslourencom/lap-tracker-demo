package com.laptracker.service;

import com.laptracker.persistence.KartRepository;
import com.laptracker.persistence.entity.Kart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KartService {

    private final KartRepository kartRepository;

    public Kart createKart(String kartNumber, Long raceId) {
        Kart kart = new Kart();
        kart.setKartNumber(kartNumber);
        kart.setRaceId(raceId);
        return kartRepository.save(kart);
    }

    public List<Kart> createKartsForRace(Long raceId, List<String> kartNumbers) {
        return kartNumbers.stream()
                .map(kartNumber -> createKart(kartNumber, raceId))
                .collect(Collectors.toList());
    }

    public List<Kart> findKartsByRaceId(Long raceId) {
        return kartRepository.findByRaceId(raceId);
    }

    public Optional<Kart> findKartByNumberAndRaceId(String kartNumber, Long raceId) {
        return kartRepository.findByKartNumberAndRaceId(kartNumber, raceId);
    }

}
