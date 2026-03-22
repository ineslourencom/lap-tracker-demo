package com.laptracker.service;

import com.laptracker.api.model.response.LapDetailResponse;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Passage;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.domain.PassageService;
import com.laptracker.service.domain.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceResultServiceTest {

    @Mock
    private PassageService passageService;

    @Mock
    private RaceService raceService;

    @InjectMocks
    private RaceResultService raceResultService;

    private Race race;
    private Kart kart1;
    private Kart kart2;

    @BeforeEach
    void setUp() {
        race = new Race();
        race.setId(UUID.randomUUID());

        kart1 = new Kart();
        kart1.setKartNumber(1);

        kart2 = new Kart();
        kart2.setKartNumber(2);
    }

    @Test
    void testGetRaceResult_SameLapsWinnerByTime() {
        var passages = List.of(
            new Passage(UUID.randomUUID(), race, kart1, LocalDateTime.of(2024, 1, 1, 12, 0, 0)),
            new Passage(UUID.randomUUID(), race, kart1, LocalDateTime.of(2024, 1, 1, 12, 1, 0)),
            new Passage(UUID.randomUUID(), race, kart1, LocalDateTime.of(2024, 1, 1, 12, 2, 0)),
            new Passage(UUID.randomUUID(), race, kart2, LocalDateTime.of(2024, 1, 1, 12, 0, 10)),
            new Passage(UUID.randomUUID(), race, kart2, LocalDateTime.of(2024, 1, 1, 12, 1, 5)),
            new Passage(UUID.randomUUID(), race, kart2, LocalDateTime.of(2024, 1, 1, 12, 1, 55))
        );

        when(raceService.findById(race.getId())).thenReturn(race);
        when(passageService.getAllByRace(race)).thenReturn(passages);

        var result = raceResultService.getRaceResult(race.getId());

        assertEquals(2, result.getWinnerKart());
        LapDetailResponse fastestLap = result.getFastestLap();
        assertEquals(2, fastestLap.getKartNumber());
        assertEquals("00:50.000", fastestLap.getDuration());
    }

    @Test
    void testGetRaceResult_WinnerByLaps() {
        var passages = List.of(
                new Passage(UUID.randomUUID(), race, kart1, LocalDateTime.of(2024, 1, 1, 12, 0, 0)),
                new Passage(UUID.randomUUID(), race, kart1, LocalDateTime.of(2024, 1, 1, 12, 1, 0)),
                new Passage(UUID.randomUUID(), race, kart1, LocalDateTime.of(2024, 1, 1, 12, 2, 0)),
                new Passage(UUID.randomUUID(), race, kart2, LocalDateTime.of(2024, 1, 1, 12, 0, 10)),
                new Passage(UUID.randomUUID(), race, kart2, LocalDateTime.of(2024, 1, 1, 12, 1, 5))
        );

        when(raceService.findById(race.getId())).thenReturn(race);
        when(passageService.getAllByRace(race)).thenReturn(passages);

        var result = raceResultService.getRaceResult(race.getId());

        assertEquals(1, result.getWinnerKart());
        var fastestLap = result.getFastestLap();
        assertEquals(2, fastestLap.getKartNumber());
        assertEquals("00:55.000", fastestLap.getDuration());
    }
}

}
