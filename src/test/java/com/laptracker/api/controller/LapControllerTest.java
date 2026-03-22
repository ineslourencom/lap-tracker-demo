package com.laptracker.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laptracker.api.dto.request.RecordPassageRequest;
import com.laptracker.service.PassageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LapController.class)
public class LapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PassageService passageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void recordPassage_ShouldReturnOk_WhenRequestIsValid() throws Exception {
        RecordPassageRequest request = new RecordPassageRequest("K123", LocalDateTime.now());

        mockMvc.perform(post("/laps/record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void recordPassage_ShouldReturnBadRequest_WhenKartNumberIsBlank() throws Exception {
        RecordPassageRequest request = new RecordPassageRequest("", LocalDateTime.now());

        mockMvc.perform(post("/laps/record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void recordPassage_ShouldReturnBadRequest_WhenTimestampIsNull() throws Exception {
        RecordPassageRequest request = new RecordPassageRequest("K123", null);

        mockMvc.perform(post("/laps/record")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
