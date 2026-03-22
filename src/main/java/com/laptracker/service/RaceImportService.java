package com.laptracker.service;

import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Lap;
import com.laptracker.persistence.entity.Race;
import com.laptracker.persistence.entity.RaceStatus;
import com.laptracker.persistence.KartRepository;
import com.laptracker.persistence.LapRepository;
import com.laptracker.persistence.RaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RaceImportService {

    private final RaceRepository raceRepository;
    private final KartRepository kartRepository;
    private final LapRepository lapRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Race importRace(InputStream inputStream) throws IOException {
        List<String[]> records = readCsv(inputStream);

        LocalDateTime raceStartTime = findRaceStartTime(records);
        Race race = createRace(raceStartTime);

        Map<Integer, List<LocalTime>> timesByKart = groupTimesByKart(records);

        for (Map.Entry<Integer, List<LocalTime>> entry : timesByKart.entrySet()) {
            Integer kartNumber = entry.getKey();
            List<LocalTime> passingTimes = entry.getValue();
            passingTimes.sort(Comparator.naturalOrder());

            Kart kart = createKart(race, kartNumber);

            createLapsForKart(race, kart, raceStartTime, passingTimes);
        }

        return race;
    }

    private List<String[]> readCsv(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
        }
    }

    private LocalDateTime findRaceStartTime(List<String[]> records) {
        return records.stream()
                .map(record -> LocalTime.parse(record[1], TIME_FORMATTER))
                .min(Comparator.naturalOrder())
                .map(localTime -> LocalDateTime.of(LocalDate.now(), localTime))
                .orElseThrow(() -> new IllegalArgumentException("Cannot determine race start time from empty CSV"));
    }


    private Race createRace(LocalDateTime startTime) {
        Race race = new Race();
        race.setRaceName("Imported Race " + LocalDateTime.now());
        race.setRaceTime(startTime);
        race.setTotalLaps(0L);
        race.setStatus(RaceStatus.FINISHED);
        return raceRepository.save(race);
    }

    private Map<Integer, List<LocalTime>> groupTimesByKart(List<String[]> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        record -> Integer.parseInt(record[0]),
                        Collectors.mapping(record -> LocalTime.parse(record[1], TIME_FORMATTER), Collectors.toList())
                ));
    }

    private Kart createKart(Race race, Integer kartNumber) {
        Kart kart = new Kart();
        kart.setRace(race);
        kart.setKartNumber(kartNumber);
        return kartRepository.save(kart);
    }

    private void createLapsForKart(Race race, Kart kart, LocalDateTime raceStartTime, List<LocalTime> passingTimes) {
        List<LocalDateTime> orderedPassingDateTimes = passingTimes.stream()
                .map(time -> LocalDateTime.of(raceStartTime.toLocalDate(), time))
                .collect(Collectors.toList());


        for (int i = 0; i < orderedPassingDateTimes.size() - 1; i++) {
            Lap lap = new Lap();
            lap.setRace(race);
            lap.setKart(kart);
            lap.setLapNumber(i + 1);
            lap.setStartTime(orderedPassingDateTimes.get(i));
            lap.setFinishTime(orderedPassingDateTimes.get(i + 1));
            lapRepository.save(lap);
        }
    }
}
