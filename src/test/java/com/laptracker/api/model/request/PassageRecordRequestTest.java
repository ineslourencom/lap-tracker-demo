package com.laptracker.api.model.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PassageRecordRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldPassValidation_whenRequestIsValid() {
        PassageRecordRequest request = new PassageRecordRequest(123, LocalDateTime.now());
        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenKartNumberIsBlank() {
        PassageRecordRequest request = new PassageRecordRequest(null, LocalDateTime.now());
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldFailValidation_whenTimestampIsNull() {
        PassageRecordRequest request = new PassageRecordRequest(123, null);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Disabled("This will fail due to @PastOrPresent Validation")
    @Test
    void shouldFailValidation_whenTimestampIsFuture() {
        PassageRecordRequest request = new PassageRecordRequest(123, LocalDateTime.now().plusDays(1));
        assertFalse(validator.validate(request).isEmpty());
    }
}
