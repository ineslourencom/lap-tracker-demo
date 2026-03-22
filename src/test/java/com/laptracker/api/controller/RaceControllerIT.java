package com.laptracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptracker.BaseITTest;
import com.laptracker.api.model.request.StartRaceRequest;
import com.laptracker.persistence.KartRepository;
import com.laptracker.persistence.PassageRepository;
import com.laptracker.persistence.RaceRepository;
import com.laptracker.util.TestFixtures;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RaceControllerIT extends BaseITTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private KartRepository kartRepository;

    @Autowired
    private PassageRepository passageRepository;

    @Test
    void startRace_ShouldReturnOk_WhenRequestIsValid() throws Exception {
        // given
        StartRaceRequest request = TestFixtures.createRaceRequest(3, 5);

        // when & then
        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

    }

    @Test
    void finishRace_ShouldReturnOk_WhenRaceExists() throws Exception {
        // given
        StartRaceRequest request = TestFixtures.createRaceRequest(3, 5);
        mockMvc.perform(post("/races")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var response = mockMvc.perform(get("/races/active")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // when & then
        mockMvc.perform(put("/races/{id}/finish", response))
                .andExpect(status().isOk());
    }
}
