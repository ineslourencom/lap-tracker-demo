package com.laptracker.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RaceResultResponse {
    private int winnerKart;
    private LapDetailResponse fastestLap;
}
