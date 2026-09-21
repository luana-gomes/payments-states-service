package com.platform.payments.domain;
import java.time.OffsetDateTime;
import java.util.Objects;


public class PaymentOperation {
	
	private final String externalReferenceId;
	private final String paymentId;
	private final String protocol;
	private final OperationType operationType;
	
	private PaymentsStatus status;
	
	private final OffsetDateTime createdAt;
	
	private OffsetDateTime updatedAt;
	
	public PaymentOperation(
			String externalReferenceId,
			String paymentId,
			String protocol,
			OperationType operationType,
			PaymentsStatus status, 
			OffsetDateTime createdAt,
			OffsetDateTime updatedAt){
		
		this.externalReferenceId = requireNonBlank(
				externalReferenceId,
				"externalReferenceId"
				);
		this.paymentId = requireNonBlank(
				paymentId, "paymentId"
				);
		this.protocol = requireNonBlank(
				protocol, "protocol"
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
	private static String requireNonBlank (String value, String fieldName) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(
					fieldName + " must not be blank"
					);
		}
		return value;
	}
	public String getExternalReferenceId() {
		return externalReferenceId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public String getProtocol() {
		return protocol;
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
