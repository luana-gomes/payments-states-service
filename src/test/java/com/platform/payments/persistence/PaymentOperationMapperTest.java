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
    };
    
    @Test 
    void shouldMapAuthorizationAndCaptureForSamePaymentCard() {
    	Long paymentCardId = 10L;
    	UUID tennatId = UUID.randomUUID();
    	OffsetDateTime createdAt = OffsetDateTime.now();
    	
    	PaymentOperation authorization = new PaymentOperation(
    			paymentCardId,
    			tennatId,
    			"1015",
    			"MP",
    			OperationType.AUTHORIZATION,
    			PaymentsStatus.APPROVED,
    			createdAt,
    			createdAt.plusMinutes(3)
    			);
    	PaymentOperation capture = new PaymentOperation(
    			paymentCardId,
    			tennatId,
    			"1016",
    			"MP",
    			OperationType.CAPTURE,
    			PaymentsStatus.APPROVED,
    			createdAt.plusMinutes(3),
    			createdAt.plusMinutes(3)
    			);
    	   PaymentOperationEntity authorizationEntity = mapper.toEntity(authorization);
    	    PaymentOperationEntity captureEntity = mapper.toEntity(capture);
    	    assertEquals(paymentCardId, authorizationEntity.getPaymentCardId());
    	    assertEquals(paymentCardId, captureEntity.getPaymentCardId());
    	    assertEquals(OperationType.AUTHORIZATION, authorizationEntity.getOperationType());
    	    assertEquals(OperationType.CAPTURE, captureEntity.getOperationType());
    };
    
    @Test
    void shouldMapEntityToDomain() {

        Long paymentCardId = 10L;
        UUID tenantId = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = createdAt.plusMinutes(5);

        PaymentOperationEntity entity = new PaymentOperationEntity();
        entity.setPaymentCardId(paymentCardId);
        entity.setTenantId(tenantId);
        entity.setExternalReferenceId("1015");
        entity.setAcquirer("MP");
        entity.setOperationType(OperationType.AUTHORIZATION);
        entity.setStatus(PaymentsStatus.APPROVED);
        entity.setCreatedAt(createdAt);
        entity.setUpdatedAt(updatedAt);

        PaymentOperation domain = mapper.toDomain(entity);

        assertEquals(paymentCardId, domain.getPaymentCardId());
        assertEquals(tenantId, domain.getTenantId());
        assertEquals("1015", domain.getExternalReferenceId());
        assertEquals("MP", domain.getAcquirer());
        assertEquals(OperationType.AUTHORIZATION, domain.getOperationType());
        assertEquals(PaymentsStatus.APPROVED, domain.getStatus());
        assertEquals(createdAt, domain.getCreatedAt());
        assertEquals(updatedAt, domain.getUpdatedAt());
    }
}