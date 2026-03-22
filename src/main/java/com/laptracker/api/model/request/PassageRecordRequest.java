package com.laptracker.api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public record RecordLapRequest(
        @NotNull(message = "Kart number is mandatory")
        Integer kartNumber,
        @NotNull(message = "Timestamp is mandatory")
        @PastOrPresent(message = "Timestamp cannot be in the future")
        LocalDateTime timestamp
) {}

