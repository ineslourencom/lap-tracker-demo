package com.laptracker.api.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LapDetailResponse {
    private int kartNumber;
    private Integer lapNumber;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private String duration;

    @JsonIgnore
    private Duration durationValue;
}
