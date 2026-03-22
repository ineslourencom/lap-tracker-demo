package com.laptracker.event;

import java.time.LocalDateTime;

public record LapRecordEvent(Integer kartNumber, LocalDateTime timestamp) {
}
