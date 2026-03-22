package com.laptracker.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LapDetailResponse {
    private Long durationMillis;
    private Integer lapNumber;
    private LocalDateTime startTime;
}
