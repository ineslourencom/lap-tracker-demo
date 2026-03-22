package com.laptracker.api.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StartRaceRequest(
        @NotBlank(message = "Name is mandatory")
        String name,
        @NotNull(message = "Total laps is mandatory")
        @Min(value = 1, message = "Total laps must be greater than 0")
        @Max(value = 200, message = "Maximum number of allowed laps is 200 ")
        Integer totalLaps,
        @NotEmpty(message = "At least one kart number is required")
        List<@NotNull(message = "Kart number cannot be null") Integer> kartNumbers
) {}
