package com.platform.payments.domain;

import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentOperationTest {

    @Test
    void shouldRejectBlankExternalReferenceId() {

        OffsetDateTime now = OffsetDateTime.now();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentOperation(
                        "",
                        "001",
                        "ABW1067R",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );

        assertEquals(
                "externalReferenceId must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankPaymentId() {

        OffsetDateTime now = OffsetDateTime.now();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentOperation(
                        "1010",
                        "",
                        "ABW1067R",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );

        assertEquals(
                "paymentId must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectBlankProtocol() {

        OffsetDateTime now = OffsetDateTime.now();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentOperation(
                        "1010",
                        "001",
                        "",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );

        assertEquals(
                "protocol must not be blank",
                exception.getMessage()
        );
    }

    @Test
    void shouldRejectNullExternalReferenceId() {

        OffsetDateTime now = OffsetDateTime.now();

        assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentOperation(
                        null,
                        "001",
                        "ABW1067R",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );
    }

    @Test
    void shouldRejectNullPaymentId() {

        OffsetDateTime now = OffsetDateTime.now();

        assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentOperation(
                        "1010",
                        null,
                        "ABW1067R",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );
    }

    @Test
    void shouldRejectNullProtocol() {

        OffsetDateTime now = OffsetDateTime.now();

        assertThrows(
                IllegalArgumentException.class,
                () -> new PaymentOperation(
                        "1010",
                        "001",
                        null,
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );
    }

    @Test
    void shouldRejectNullOperationType() {

        OffsetDateTime now = OffsetDateTime.now();

        assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        "1010",
                        "001",
                        "ABW1067R",
                        null,
                        PaymentsStatus.PENDING,
                        now,
                        now
                )
        );
    }

    @Test
    void shouldRejectNullStatus() {

        OffsetDateTime now = OffsetDateTime.now();

        assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        "1010",
                        "001",
                        "ABW1067R",
                        OperationType.AUTHORIZATION,
                        null,
                        now,
                        now
                )
        );
    }

    @Test
    void shouldRejectNullCreatedAt() {

        OffsetDateTime now = OffsetDateTime.now();

        assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        "1010",
                        "001",
                        "ABW1067R",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        null,
                        now
                )
        );
    }

    @Test
    void shouldRejectNullUpdatedAt() {

        OffsetDateTime now = OffsetDateTime.now();

        assertThrows(
                NullPointerException.class,
                () -> new PaymentOperation(
                        "1010",
                        "001",
                        "ABW1067R",
                        OperationType.AUTHORIZATION,
                        PaymentsStatus.PENDING,
                        now,
                        null
                )
        );
    }

    @Test
    void shouldCreateAuthorizationOperation() {

        OffsetDateTime now = OffsetDateTime.now();

        PaymentOperation operation = new PaymentOperation(
                "1010",
                "001",
                "ABW1067R",
                OperationType.AUTHORIZATION,
                PaymentsStatus.PENDING,
                now,
                now
        );

        assertEquals("1010", operation.getExternalReferenceId());
        assertEquals("001", operation.getPaymentId());
        assertEquals("ABW1067R", operation.getProtocol());
        assertEquals(OperationType.AUTHORIZATION, operation.getOperationType());
        assertEquals(PaymentsStatus.PENDING, operation.getStatus());
        assertEquals(now, operation.getCreatedAt());
        assertEquals(now, operation.getUpdatedAt());
    }

    @Test
    void shouldCreateCaptureOperation() {

        OffsetDateTime now = OffsetDateTime.now();

        PaymentOperation operation = new PaymentOperation(
                "1011",
                "001",
                "ABW1067R",
                OperationType.CAPTURE,
                PaymentsStatus.PENDING,
                now,
                now
        );

        assertEquals("1011", operation.getExternalReferenceId());
        assertEquals("001", operation.getPaymentId());
        assertEquals("ABW1067R", operation.getProtocol());
        assertEquals(OperationType.CAPTURE, operation.getOperationType());
        assertEquals(PaymentsStatus.PENDING, operation.getStatus());
        assertEquals(now, operation.getCreatedAt());
        assertEquals(now, operation.getUpdatedAt());
    }

    @Test
    void shouldCreateAuthorizationAndCaptureWithSameProtocol() {

        OffsetDateTime now = OffsetDateTime.now();

        PaymentOperation authorization = new PaymentOperation(
                "1012",
                "001",
                "ABW1067R",
                OperationType.AUTHORIZATION,
                PaymentsStatus.PENDING,
                now,
                now
        );

        PaymentOperation capture = new PaymentOperation(
                "1013",
                "001",
                "ABW1067R",
                OperationType.CAPTURE,
                PaymentsStatus.PENDING,
                now,
                now
        );

        assertEquals(
                authorization.getProtocol(),
                capture.getProtocol()
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
