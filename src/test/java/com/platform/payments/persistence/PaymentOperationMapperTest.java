package com.platform.payments.persistence;

import com.platform.payments.domain.OperationType;
import com.platform.payments.domain.PaymentOperation;
import com.platform.payments.domain.PaymentsStatus;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentOperationMapperTest {

    private final PaymentOperationMapper mapper = new PaymentOperationMapper();

    @Test
    void shouldMapDomainToEntity() {

        Long paymentCardId = 10L;
        UUID tenantId = UUID.randomUUID();
        String externalReferenceId = "1015";
        String acquirer = "MP";
        OperationType operationType = OperationType.AUTHORIZATION;
        PaymentsStatus status = PaymentsStatus.APPROVED;
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = createdAt.plusMinutes(5);

        PaymentOperation domain = new PaymentOperation(
                paymentCardId,
                tenantId,
                externalReferenceId,
                acquirer,
                operationType,
                status,
                createdAt,
                updatedAt
        );

        PaymentOperationEntity entity = mapper.toEntity(domain);

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