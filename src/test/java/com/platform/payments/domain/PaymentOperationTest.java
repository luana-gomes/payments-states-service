package com.platform.payments.domain;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentOperationTest {

    private static final Long PAYMENT_CARD_ID = 10500L;
    private static final UUID TENANT_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Test
    void shouldRejectNullPaymentCardId() {

        OffsetDateTime now = OffsetDateTime.now();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        null,
                        TENANT_ID,
                        "1015",
                        "MP",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );

        assertEquals(
                "paymentCardId must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullTenantId() {

        OffsetDateTime now = OffsetDateTime.now();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        PAYMENT_CARD_ID,
                        null,
                        "1015",
                        "MP",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );

        assertEquals(
                "tenantId must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldAllowNullExternalReferenceIdAndAcquirer() {

        OffsetDateTime now = OffsetDateTime.now();

        PaymentOperation operation = new PaymentOperation(
                PAYMENT_CARD_ID,
                TENANT_ID,
                null,
                null,
                OperationType.AUTHORIZATION,
                PaymentsStatus.PENDING,
                now,
                now
        );

        assertNull(operation.getExternalReferenceId());
        assertNull(operation.getAcquirer());
    }

    @Test
    void shouldRejectNullOperationType() {

        OffsetDateTime now = OffsetDateTime.now();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        PAYMENT_CARD_ID,
                        TENANT_ID,
                        "1015",
                        "MP",
                        null,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );

        assertEquals(
                "operationType must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullStatus() {

        OffsetDateTime now = OffsetDateTime.now();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        PAYMENT_CARD_ID,
                        TENANT_ID,
                        "1015",
                        "MP",
                        OperationType.AUTHORIZATION,
                        null,
                        now,
                        now
                )
        );

        assertEquals(
                "status must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullCreatedAt() {

        OffsetDateTime now = OffsetDateTime.now();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        PAYMENT_CARD_ID,
                        TENANT_ID,
                        "1015",
                        "MP",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        null,
                        now
                )
        );

        assertEquals(
                "createdAt must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullUpdatedAt() {

        OffsetDateTime now = OffsetDateTime.now();

        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        PAYMENT_CARD_ID,
                        TENANT_ID,
                        "1015",
                        "MP",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        null
                )
        );

        assertEquals(
                "updatedAt must not be null",
                exception.getMessage()
        );
    }

    @Test
    void shouldCreateAuthorizationOperation() {

        OffsetDateTime now = OffsetDateTime.now();

        PaymentOperation operation = new PaymentOperation(
                PAYMENT_CARD_ID,
                TENANT_ID,
                "1015",
                "MP",
                OperationType.AUTHORIZATION,
                PaymentsStatus.PENDING,
                now,
                now
        );

        assertEquals(PAYMENT_CARD_ID, operation.getPaymentCardId());
        assertEquals(TENANT_ID, operation.getTenantId());
        assertEquals("1015", operation.getExternalReferenceId());
        assertEquals("MP", operation.getAcquirer());
        assertEquals(OperationType.AUTHORIZATION, operation.getOperationType());
        assertEquals(PaymentsStatus.PENDING, operation.getStatus());
        assertEquals(now, operation.getCreatedAt());
        assertEquals(now, operation.getUpdatedAt());
    }

    @Test
    void shouldCreateCaptureOperation() {

        OffsetDateTime now = OffsetDateTime.now();

        PaymentOperation operation = new PaymentOperation(
                PAYMENT_CARD_ID,
                TENANT_ID,
                "1016",
                "MP",
                OperationType.CAPTURE,
                PaymentsStatus.PENDING,
                now,
                now
        );

        assertEquals(PAYMENT_CARD_ID, operation.getPaymentCardId());
        assertEquals(TENANT_ID, operation.getTenantId());
        assertEquals("1016", operation.getExternalReferenceId());
        assertEquals("MP", operation.getAcquirer());
        assertEquals(OperationType.CAPTURE, operation.getOperationType());
        assertEquals(PaymentsStatus.PENDING, operation.getStatus());
        assertEquals(now, operation.getCreatedAt());
        assertEquals(now, operation.getUpdatedAt());
    }

    @Test
    void shouldCreateAuthorizationAndCaptureForSamePaymentCard() {

        OffsetDateTime now = OffsetDateTime.now();

        PaymentOperation authorization = new PaymentOperation(
                PAYMENT_CARD_ID,
                TENANT_ID,
                "1015",
                "MP",
                OperationType.AUTHORIZATION,
                PaymentsStatus.PENDING,
                now,
                now
        );

        PaymentOperation capture = new PaymentOperation(
                PAYMENT_CARD_ID,
                TENANT_ID,
                "1016",
                "MP",
                OperationType.CAPTURE,
                PaymentsStatus.PENDING,
                now,
                now
        );

        assertEquals(
                authorization.getPaymentCardId(),
                capture.getPaymentCardId()
        );

        assertEquals(
                authorization.getTenantId(),
                capture.getTenantId()
        );

        assertEquals(
                OperationType.AUTHORIZATION,
                authorization.getOperationType()
        );

        assertEquals(
                OperationType.CAPTURE,
                capture.getOperationType()
        );
    }
}
