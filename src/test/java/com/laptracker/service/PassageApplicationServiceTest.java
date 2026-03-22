package com.laptracker.service;

import com.laptracker.persistence.entity.Kart;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.domain.KartService;
import com.laptracker.service.domain.PassageService;
import com.laptracker.service.domain.RaceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LapRecordApplicationServiceTest {

    @Mock
    private RaceService raceService;

    @Mock
    private KartService kartService;

    @Mock
    private PassageService passageService;

    @InjectMocks
    private LapRecordApplicationService lapRecordApplicationService;

    @Test
    void recordLap_shouldRecordPassage() {
        // given
        Integer kartNumber = 1;
        LocalDateTime timestamp = LocalDateTime.now();
        Race activeRace = new Race();
        activeRace.setId(1L);
        Kart kart = new Kart();
        kart.setId(1L);

        when(raceService.getActiveRace()).thenReturn(activeRace);
        when(kartService.findKartByNumberAndRaceId(eq(kartNumber), eq(activeRace.getId()))).thenReturn(Optional.of(kart));

        // when
        lapRecordApplicationService.recordLap(kartNumber, timestamp);

        // then
        verify(passageService).recordPassage(kart, activeRace, timestamp);
    }
}
