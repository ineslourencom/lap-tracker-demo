package com.laptracker.service;

import com.laptracker.api.model.response.LapDetailResponse;
import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.domain.PassageService;
import com.laptracker.service.domain.RaceService;
import com.laptracker.util.TestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RaceResultServiceTest {

    private static final LocalDateTime RACE_START = LocalDateTime.of(2024, 1, 1, 12, 0, 0);

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
        race = TestFixtures.newRace(RACE_START);
        kart1 = TestFixtures.newKart(1, race);
        kart2 = TestFixtures.newKart(2, race);
    }

    @Test
    void testGetRaceResult_SameLapsWinnerByTime() {
        // given
        var passages = List.of(
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(60)),
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(120)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(55)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(110))
        );

        when(raceService.findById(race.getId())).thenReturn(race);
        when(passageService.getAllByRace(race)).thenReturn(passages);

        // when
        var result = raceResultService.getRaceResult(race.getId());

        // then
        assertEquals(2, result.getWinnerKart());
        LapDetailResponse fastestLap = result.getFastestLap();
        assertEquals(2, fastestLap.getKartNumber());
        assertEquals("00:55.000", fastestLap.getDuration());
    }

    @Test
    void testGetRaceResult_WinnerBySumOfLaps() {
        // given
        var passages = List.of(
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(20)),
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(40)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(15)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(45))
        );

        when(raceService.findById(race.getId())).thenReturn(race);
        when(passageService.getAllByRace(race)).thenReturn(passages);

        // when
        var result = raceResultService.getRaceResult(race.getId());

        // then
        assertEquals(1, result.getWinnerKart());
        var fastestLap = result.getFastestLap();
        assertEquals(2, fastestLap.getKartNumber());
        assertEquals("00:15.000", fastestLap.getDuration());
    }

    @Test
    void testGetRaceResult_WinnerByLaps() {
        // given
        var passages = List.of(
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(30)),
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(60)),
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(90)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(25)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(50))
        );

        when(raceService.findById(race.getId())).thenReturn(race);
        when(passageService.getAllByRace(race)).thenReturn(passages);

        // when
        var result = raceResultService.getRaceResult(race.getId());

        // then
        assertEquals(1, result.getWinnerKart());
        var fastestLap = result.getFastestLap();
        assertEquals(2, fastestLap.getKartNumber());
        assertEquals("00:25.000", fastestLap.getDuration());
    }

    @Test
    void testGetRaceResult_RaceWithPredefinedLaps_WinnerCompletesLapsFirst() {
        // given
        race.setTotalLaps(2);

        var passages = List.of(
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(20)),
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(40)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(15)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(45))
        );

        when(raceService.findById(race.getId())).thenReturn(race);
        when(passageService.getAllByRace(race)).thenReturn(passages);

        // when
        var result = raceResultService.getRaceResult(race.getId());

        // then
        assertEquals(1, result.getWinnerKart());
        var fastestLap = result.getFastestLap();
        assertEquals(2, fastestLap.getKartNumber());
        assertEquals("00:15.000", fastestLap.getDuration());
    }

    @Test
    void testGetRaceResult_TieInLapsAndDuration() {
        // given
        var passages = List.of(
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(20)),
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(40)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(15)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(40))
        );

        when(raceService.findById(race.getId())).thenReturn(race);
        when(passageService.getAllByRace(race)).thenReturn(passages);

        // when
        var result = raceResultService.getRaceResult(race.getId());

        // then
        assertEquals(1, result.getWinnerKart());
    }

    @Test
    void testWinnerWithExtraLaps() {
        // given
        race.setTotalLaps(3);

        var passages = List.of(
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(10)),
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(20)),
                TestFixtures.newPassage(race, kart1, RACE_START.plusSeconds(30)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(15)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(25)),
                TestFixtures.newPassage(race, kart2, RACE_START.plusSeconds(40))
        );

        when(raceService.findById(race.getId())).thenReturn(race);
        when(passageService.getAllByRace(race)).thenReturn(passages);

        // when
        var result = raceResultService.getRaceResult(race.getId());

        // then
        assertEquals(1, result.getWinnerKart());
    }
}




