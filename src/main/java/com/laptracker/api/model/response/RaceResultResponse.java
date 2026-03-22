package com.laptracker.api.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RaceResultResponse {
    private String raceName;
    private String kartNumber; // Renamed from winnerKartNumber to match test expectation getKartNumber()
    private LapDetailResponse fastestLap;
    private List<String> kartParticipants;
}
