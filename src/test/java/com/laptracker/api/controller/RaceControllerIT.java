package com.laptracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptracker.BaseITTest;
import com.laptracker.api.model.request.StartRaceRequest;
import com.laptracker.api.model.response.RaceResultResponse;
import com.laptracker.service.RaceApplicationService;
import com.laptracker.service.RaceResultService;
import com.laptracker.util.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RaceControllerIT extends BaseITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RaceApplicationService raceApplicationService;

    @MockBean
    private RaceResultService raceResultService;

    @Test
    void startRace_ShouldReturnOk_WhenRequestIsValid() throws Exception {
        // given
        StartRaceRequest request = TestFixtures.createRaceRequest(3, 5);
        RaceResultResponse response = TestFixtures.createRaceResultResponse("KartLeMans", null);

        when(raceApplicationService.startRace(any(StartRaceRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.raceName").value("KartLeMans"));
    }

    @Test
    void finishRace_ShouldReturnOk_WhenRaceExists() throws Exception {
        // given
        Long raceId = 1L;
        RaceResultResponse raceResponse = TestFixtures.createRaceResultResponse("KartLeMans", null);
        RaceResultResponse winnerResponse = TestFixtures.createRaceResultResponse("KartLeMans", "1");
        
        when(raceApplicationService.finishRace(eq(raceId))).thenReturn(raceResponse);
        when(raceResultService.getWinner(eq(raceId))).thenReturn(winnerResponse); // Mock getWinner too

        // when & then
        mockMvc.perform(put("/races/{id}/finish", raceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.raceName").value("KartLeMans"))
                .andExpect(jsonPath("$.kartNumber").value("1"));
    }

    @Test
    void getRaceResults_ShouldReturnOk() throws Exception {
        // given
        Long raceId = 1L;
        RaceResultResponse result = TestFixtures.createRaceResultResponse("KartLeMans", "1");
        List<RaceResultResponse> results = Collections.singletonList(result);

        when(raceResultService.getRaceResults(eq(raceId))).thenReturn(results);

        // when & then
        mockMvc.perform(get("/races/{id}/results", raceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].raceName").value("KartLeMans"));
    }
}
