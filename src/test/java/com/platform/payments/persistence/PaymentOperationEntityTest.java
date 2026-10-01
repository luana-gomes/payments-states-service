package com.platform.payments.persistence;

import com.platform.payments.domain.OperationType;
import com.platform.payments.domain.PaymentsStatus;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentOperationEntityTest {

    @Test
    void shouldSetAndGetPaymentOperationData() {

        Long paymentCardId = 10L;
        UUID tenantId = UUID.randomUUID();
        String externalReferenceId = "1015";
        String acquirer = "MP";
        OperationType operationType = OperationType.AUTHORIZATION;
        PaymentsStatus status = PaymentsStatus.APPROVED;
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = createdAt.plusMinutes(5);

        PaymentOperationEntity entity = new PaymentOperationEntity();

        entity.setPaymentCardId(paymentCardId);
        entity.setTenantId(tenantId);
        entity.setExternalReferenceId(externalReferenceId);
        entity.setAcquirer(acquirer);
        entity.setOperationType(operationType);
        entity.setStatus(status);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        assertEquals(paymentCardId, entity.getPaymentCardId());
        assertEquals(tenantId, entity.getTenantId());
        assertEquals(externalReferenceId, entity.getExternalReferenceId());
        assertEquals(acquirer, entity.getAcquirer());
        assertEquals(operationType, entity.getOperationType());
        assertEquals(status, entity.getStatus());
        assertEquals(createdAt, entity.getCreatedAt());
        assertEquals(updatedAt, entity.getUpdatedAt());
    }
}