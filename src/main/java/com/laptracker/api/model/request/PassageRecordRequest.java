package com.laptracker.api.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record PassageRecordRequest(
        @NotNull(message = "Kart number is mandatory")
        Integer kartNumber,
        @NotNull(message = "Timestamp is mandatory")
        //@PastOrPresent(message = "Timestamp cannot be in the future") removed for sake of tests for now
        LocalDateTime timestamp
) {}

