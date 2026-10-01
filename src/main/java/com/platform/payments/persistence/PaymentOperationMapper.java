package com.platform.payments.persistence;

import com.platform.payments.domain.PaymentOperation;

public class PaymentOperationMapper {

    public PaymentOperationEntity toEntity(PaymentOperation domain) {

        PaymentOperationEntity entity = new PaymentOperationEntity();

        entity.setPaymentCardId(domain.getPaymentCardId());
        entity.setTenantId(domain.getTenantId());
        entity.setExternalReferenceId(domain.getExternalReferenceId());
        entity.setAcquirer(domain.getAcquirer());
        entity.setOperationType(domain.getOperationType());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    public PaymentOperation toDomain(PaymentOperationEntity entity) {

        return new PaymentOperation(
                entity.getPaymentCardId(),
                entity.getTenantId(),
                entity.getExternalReferenceId(),
                entity.getAcquirer(),
                entity.getOperationType(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}