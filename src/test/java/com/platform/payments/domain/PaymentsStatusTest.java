package com.platform.payments.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PaymentsStatusTest {

    @Test
    void shouldHavePendingStatus() {
        assertNotNull(PaymentsStatus.PENDING);
    }

    @Test
    void shouldHaveApprovedStatus() {
        assertNotNull(PaymentsStatus.APPROVED);
    }

    @Test
    void shouldHaveDeniedStatus() {
        assertNotNull(PaymentsStatus.DENIED);
    }

    @Test
    void shouldHaveExactlyExpectedPaymentStatuses() {
        assertEquals(
                Set.of(
                        PaymentsStatus.PENDING,
                        PaymentsStatus.APPROVED,
                        PaymentsStatus.DENIED
                ),
                Set.of(PaymentsStatus.values())
        );
    }
}