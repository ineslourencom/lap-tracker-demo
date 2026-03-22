package com.laptracker.config;

import com.laptracker.persistence.RaceRepository;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.RaceImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CsvDataIngester implements CommandLineRunner {

    private final RaceImportService raceImportService;
    private final RaceRepository raceRepository;

    @Override
    public void run(String... args) throws Exception {
        if (raceRepository.count() == 0) {
            Race race = raceImportService.importRace();
            log.info("Race imported with ID: {}", race.getId());
        }
    }
}
