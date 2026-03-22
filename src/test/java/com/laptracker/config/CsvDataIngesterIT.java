package com.laptracker.config;

import com.laptracker.BaseITTest;
import com.laptracker.persistence.RaceRepository;
import com.laptracker.persistence.entity.Race;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DataSeederIT extends BaseITTest {

    @Autowired
    private RaceRepository raceRepository;

    @Test
    void shouldSeedDataOnStartup() {
        // When the application context starts (which happens before this test method),
        // DataSeeder should have run and imported the race from the CSV.

        // Then
        List<Race> races = raceRepository.findAll();
        assertThat(races).isNotEmpty();

        Race importedRace = races.get(0);
        assertThat(importedRace.getRaceName()).startsWith("Imported Race");
        assertThat(importedRace.getTotalLaps()).isGreaterThan(0);
    }
}
