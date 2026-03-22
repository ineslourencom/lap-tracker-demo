package com.laptracker.service;

import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Race;
import com.laptracker.persistence.entity.RaceStatus;
import com.laptracker.persistence.RaceRepository;
import com.laptracker.service.domain.KartService;
import com.laptracker.service.domain.PassageService;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RaceImportService {

    private final RaceRepository raceRepository;
    private final KartService kartService;
    private final PassageService passageService;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Transactional
    public Race importRace() throws IOException {
        try (InputStream inputStream = getClass().getResourceAsStream("/karttimes.csv")) {
            if (inputStream == null) {
                throw new IOException("Resource not found: /karttimes.csv");
            }
            List<String[]> records = readCsv(inputStream);

            List<String[]> dataRecords = records.stream()
                    .filter(record -> !record[0].equalsIgnoreCase("kart"))
                    .collect(Collectors.toList());

            if (dataRecords.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty or contains only header");
            }

            LocalDateTime raceStartTime = findRaceStartTime(dataRecords);

            Map<Integer, List<LocalTime>> timesByKart = groupTimesByKart(dataRecords);

            // calculate total laps as max passages - 1
            int maxPassages = timesByKart.values().stream()
                    .mapToInt(List::size)
                    .max()
                    .orElse(0);
            int totalLaps = Math.max(0, maxPassages - 1);

            Race race = createRace(raceStartTime, totalLaps);

            // create karts
            Set<Integer> kartNumbers = timesByKart.keySet();
            Map<Integer, Kart> karts = kartNumbers.stream()
                    .collect(Collectors.toMap(
                            kartNumber -> kartNumber,
                            kartNumber -> kartService.createKart(kartNumber, race)
                    ));

            // create passages
            for (String[] record : dataRecords) {
                Integer kartNumber = Integer.parseInt(record[0]);
                LocalTime time = LocalTime.parse(record[1], TIME_FORMATTER);
                LocalDateTime timestamp = LocalDateTime.of(LocalDate.now(), time);

                Kart kart = karts.get(kartNumber);
                passageService.recordPassage(kart, race, timestamp);
            }
            return race;
        }
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

    private Race createRace(LocalDateTime startTime, Integer totalLaps) {
        Race race = new Race("Imported Race " + LocalDateTime.now(), totalLaps, RaceStatus.FINISHED, startTime);
        return raceRepository.save(race);
    }

    private Map<Integer, List<LocalTime>> groupTimesByKart(List<String[]> records) {
        return records.stream()
                .collect(Collectors.groupingBy(
                        record -> Integer.parseInt(record[0]),
                        Collectors.mapping(record -> LocalTime.parse(record[1], TIME_FORMATTER), Collectors.toList())
                ));
    }
}
