package com.laptracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptracker.api.model.request.PassageRecordRequest;
import com.laptracker.api.model.request.StartRaceRequest;
import com.laptracker.persistence.KartRepository;
import com.laptracker.persistence.PassageRepository;
import com.laptracker.persistence.RaceRepository;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.domain.RaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class EndToEndTest extends BaseITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RaceService raceService;

    @Autowired
    private PassageRepository passageRepository;

    @Autowired
    private KartRepository kartRepository;

    @Autowired
    private RaceRepository raceRepository;

    @BeforeEach
    void setUp() {
        passageRepository.deleteAll();
        kartRepository.deleteAll();
        raceRepository.deleteAll();
    }

    @Test
    void shouldSimulateRaceWithPredefinedNumberOfLaps() throws Exception {
        // given
        // 1. start a race
        StartRaceRequest startRaceRequest = new StartRaceRequest("Test Race", 2, List.of(1, 2));
        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRaceRequest)))
                .andExpect(status().isOk());

        Race activeRace = raceService.getActiveRace();
        LocalDateTime raceTime = LocalDateTime.now();

        // 2. record passages for each kart
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(1, raceTime.plusSeconds(50)))))
                .andExpect(status().isAccepted());
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(1, raceTime.plusSeconds(100)))))
                .andExpect(status().isAccepted());

        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(2, raceTime.plusSeconds(55)))))
                .andExpect(status().isAccepted());
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(2, raceTime.plusSeconds(115)))))
                .andExpect(status().isAccepted());


        await().atMost(Duration.ofSeconds(5))
                .until(() -> passageRepository.getPassagesByRace(activeRace).size() == 4);

        // 3. check race results
        mockMvc.perform(put("/races/{id}/finish", activeRace.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winnerKart").value(1))
                .andExpect(jsonPath("$.fastestLap.duration").value("00:50.000"))
                .andExpect(jsonPath("$.fastestLap.lapNumber").value(2));

    }
}
