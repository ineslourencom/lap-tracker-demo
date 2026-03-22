package com.laptracker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptracker.BaseITTest;
import com.laptracker.api.model.request.PassageRecordRequest;
import com.laptracker.api.model.request.StartRaceRequest;
import com.laptracker.persistence.entity.Race;
import com.laptracker.service.domain.RaceService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class FullRaceIntegrationTest extends BaseITTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RaceService raceService;


    @Test
    void shouldSimulateRaceWithPredefinedNumberOfLaps() throws Exception {
        // given
        // 1. Start a race
        StartRaceRequest startRaceRequest = new StartRaceRequest("Test Race", 2, List.of(1, 2));
        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRaceRequest)))
                .andExpect(status().isOk());

        Race activeRace = raceService.getActiveRace();
        LocalDateTime raceTime = LocalDateTime.now();

        // 2. Record passages for each kart
        // Kart 1
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(1, raceTime.plusSeconds(50)))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(1, raceTime.plusSeconds(100)))))
                .andExpect(status().isOk());
        // Kart 2
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(2, raceTime.plusSeconds(55)))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(2, raceTime.plusSeconds(115)))))
                .andExpect(status().isOk());


        // 3. Check race results
        mockMvc.perform(put("/races/{id}/finish", activeRace.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winnerKart").value(1))
                .andExpect(jsonPath("$.fastestLap.lapNumber").value(1))
                .andExpect(jsonPath("$.fastestLap.duration").value(50000));

    }

    @Test
    void shouldSimulateRaceWithUndefinedNumberOfLaps() throws Exception {
        // given
        // 1. Start a race
        StartRaceRequest startRaceRequest = new StartRaceRequest("Test Race", 0, List.of(1, 2));
        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRaceRequest)))
                .andExpect(status().isOk());

        Race activeRace = raceService.getActiveRace();
        LocalDateTime raceTime = LocalDateTime.now();

        // 2. Record passages for each kart
        // Kart 1
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(1, raceTime.plusSeconds(50)))))
                .andExpect(status().isOk());
        // Kart 2
        mockMvc.perform(post("/passages/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new PassageRecordRequest(2, raceTime.plusSeconds(55)))))
                .andExpect(status().isOk());


        // 3. Check race results
        mockMvc.perform(get("/races/{id}/results", activeRace.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winnerKart").value(1))
                .andExpect(jsonPath("$.fastestLap.lapNumber").value(1))
                .andExpect(jsonPath("$.fastestLap.duration").value(50000));

        // 4. Finish the race
        mockMvc.perform(put("/races/{id}/finish", activeRace.getId()))
                .andExpect(status().isOk());
    }
}
