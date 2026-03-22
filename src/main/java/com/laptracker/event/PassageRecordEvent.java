package com.laptracker.event;

import java.time.LocalDateTime;

public record PassageRecordEvent(Integer kartNumber, LocalDateTime timestamp) {
}
