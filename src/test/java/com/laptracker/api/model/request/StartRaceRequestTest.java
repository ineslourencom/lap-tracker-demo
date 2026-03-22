package com.laptracker.api.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StartRaceRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_whenRequestIsValid() {
        StartRaceRequest request = new StartRaceRequest("Grand Prix", 10, List.of("1", "2"));
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenNameIsBlank() {
        StartRaceRequest request = new StartRaceRequest("", 10, List.of("1", "2"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenTotalLapsIsZero() {
        StartRaceRequest request = new StartRaceRequest("Grand Prix", 0, List.of("1", "2"));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenKartNumbersIsEmpty() {
        StartRaceRequest request = new StartRaceRequest("Grand Prix", 10, Collections.emptyList());
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenKartNumberIsBlank() {
        StartRaceRequest request = new StartRaceRequest("Grand Prix", 10, List.of(""));
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenTotalLapsIsNull() {
        StartRaceRequest request = new StartRaceRequest("Grand Prix", null, List.of("1", "2"));
        assertFalse(validator.validate(request).isEmpty());
    }
}
