package com.platform.payments.domain;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class PaymentOperation {

    private final Long paymentCardId;
    private final UUID tenantId;
    private final String externalReferenceId;
    private final String acquirer;
    private final OperationType operationType;

    private PaymentsStatus status;

    private final OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public PaymentOperation(
            Long paymentCardId,
            UUID tenantId,
            String externalReferenceId,
            String acquirer,
            OperationType operationType,
            PaymentsStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {

        this.paymentCardId = Objects.requireNonNull(
                paymentCardId,
                "paymentCardId must not be null"
        );

        this.tenantId = Objects.requireNonNull(
                tenantId,
                "tenantId must not be null"
        );

        this.externalReferenceId = Objects.requireNonNull(
                externalReferenceId,
                "externalReferenceId must not be null"
        );

        this.acquirer = Objects.requireNonNull(
                acquirer,
                "acquirer must not be null"
        );

        this.operationType = Objects.requireNonNull(
                operationType,
                "operationType must not be null"
        );

        this.status = Objects.requireNonNull(
                status,
                "status must not be null"
        );

        this.createdAt = Objects.requireNonNull(
                createdAt,
                "createdAt must not be null"
        );

        this.updatedAt = Objects.requireNonNull(
                updatedAt,
                "updatedAt must not be null"
        );
    }

    public Long getPaymentCardId() {
        return paymentCardId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    public String getAcquirer() {
        return acquirer;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public PaymentsStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}
