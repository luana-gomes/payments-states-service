package com.platform.payments.persistence;
import com.platform.payments.domain.OperationType;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOperationRepository extends JpaRepository<PaymentOperationEntity,Long>{
	List<PaymentOperationEntity> findByExternalReferenceIdAndAcquirerAndPaymentCardId(
			String externalReferenceId,
			String acquirer,
			Long PaymentCardId
			
			);
	List<PaymentOperationEntity> findByPaymentCardIdAndOperationType(
			Long paymentCardId,
			OperationType operationType
			
			);

}
