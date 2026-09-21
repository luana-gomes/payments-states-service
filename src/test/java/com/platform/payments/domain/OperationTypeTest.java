package com.platform.payments.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OperationTypeTest {

    @Test
    void shouldHaveAuthorizationOperationType() {
        assertNotNull(OperationType.AUTHORIZATION);
    }

    @Test
    void shouldHaveCaptureOperationType() {
        assertNotNull(OperationType.CAPTURE);
    }

    @Test
    void shouldHaveExactlyExpectedOperationTypes() {
        assertEquals(
                Set.of(
                        OperationType.AUTHORIZATION,
                        OperationType.CAPTURE
                ),
                Set.of(OperationType.values())
        );
    }
}