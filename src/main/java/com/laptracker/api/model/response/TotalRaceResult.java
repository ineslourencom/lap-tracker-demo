package com.laptracker.api.model.response;

import java.time.Duration;

public record TotalRaceResult(Duration totalDuration, int lapCount) {
}