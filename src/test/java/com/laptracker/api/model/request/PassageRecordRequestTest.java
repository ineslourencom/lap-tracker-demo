package com.laptracker.api.model.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecordLapRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_whenRequestIsValid() {
        RecordLapRequest request = new RecordLapRequest(123, LocalDateTime.now());
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenKartNumberIsBlank() {
        RecordLapRequest request = new RecordLapRequest(null, LocalDateTime.now());
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenTimestampIsNull() {
        RecordLapRequest request = new RecordLapRequest(123, null);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenTimestampIsFuture() {
        RecordLapRequest request = new RecordLapRequest(123, LocalDateTime.now().plusDays(1));
        assertFalse(validator.validate(request).isEmpty());
    }
}
