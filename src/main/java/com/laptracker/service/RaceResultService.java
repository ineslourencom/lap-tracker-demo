package com.laptracker.service;

import com.laptracker.api.dto.response.RaceResultResponse;
import com.laptracker.mapper.RaceMapper;
import com.laptracker.persistence.KartRepository;
import com.laptracker.persistence.LapRepository;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Lap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RaceResultService {

    private final LapRepository lapRepository;
    private final KartRepository kartRepository;
    private final RaceMapper raceMapper;

    public List<RaceResultResponse> getRaceResults(Long raceId) {
        List<Kart> karts = kartRepository.findByRaceId(raceId);
        if (karts.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> kartIds = karts.stream().map(Kart::getId).collect(Collectors.toList());
        List<Lap> allLaps = lapRepository.findByKartIdIn(kartIds);

        List<RaceResultResponse> results = new ArrayList<>();

        for (Kart kart : karts) {
            List<Lap> kartLaps = allLaps.stream()
                .filter(l -> l.getKartId().equals(kart.getId()))
                .filter(l -> l.getFinishTime() != null)
                .toList();

            if (kartLaps.isEmpty()) {
                continue;
            }

            Lap fastestLap = kartLaps.stream()
                .min(Comparator.comparing(l -> Duration.between(l.getStartTime(), l.getFinishTime())))
                .orElse(null);

            if (fastestLap != null) {
                results.add(raceMapper.toRaceResultResponse(kart, fastestLap));
            }
        }

        results.sort(Comparator.comparingLong(r -> r.getFastestLap().getDurationMillis()));

        return results;
    }

    @Transactional(readOnly = true)
    public RaceResultResponse getWinner(Long raceId) {
        return getRaceResults(raceId).stream()
            .min(Comparator.comparingLong(r -> r.getFastestLap().getDurationMillis()))
            .orElse(null);
    }
}
