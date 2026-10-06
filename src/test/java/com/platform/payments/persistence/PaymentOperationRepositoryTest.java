package com.platform.payments.persistence;

import com.platform.payments.domain.OperationType;
import com.platform.payments.domain.PaymentOperation;
import com.platform.payments.domain.PaymentsStatus;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(properties = {
		"spring.jpa.hibernate.ddl-auto=none",
		"spring.flyway.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
class PaymentOperationRepositoryTest {

	private static final Logger log = LoggerFactory.getLogger(PaymentOperationRepositoryTest.class);
	private static final Path RESULT_LOG = Path.of("target", "payment-operation-repository.log");
	private static final UUID TENANT_ID = UUID.fromString("267970e2-0451-4c53-ac1a-3a351a6a9dbe");
	private static final String ACQUIRER = "MP";
	private static final OffsetDateTime AT_2130 = OffsetDateTime.of(2026, 9, 21, 21, 30, 0, 0, ZoneOffset.ofHours(-3));
	private static final OffsetDateTime AT_2133 = OffsetDateTime.of(2026, 9, 21, 21, 33, 0, 0, ZoneOffset.ofHours(-3));
	private static final OffsetDateTime AT_2138 = OffsetDateTime.of(2026, 9, 21, 21, 38, 0, 0, ZoneOffset.ofHours(-3));

	private final PaymentOperationMapper mapper = new PaymentOperationMapper();

	@Autowired
	private PaymentOperationRepository repository;

	@Autowired
	private EntityManager entityManager;

	@BeforeAll
	static void resetLog() throws IOException {
		Files.createDirectories(RESULT_LOG.getParent());
		Files.writeString(RESULT_LOG, "");
	}

	@Test
	void shouldPersistApprovedAuthorizationAndApprovedCaptureForSamePaymentCard() {
		List<PaymentOperationEntity> rows = rowsThatWouldBeInserted(
				"caminho feliz 1",
				operation(1050L, "1015", OperationType.AUTHORIZATION, PaymentsStatus.APPROVED, AT_2130, AT_2133),
				operation(1050L, "1015", OperationType.CAPTURE, PaymentsStatus.APPROVED, AT_2133, AT_2133)
		);

		assertEquals(1050L, rows.get(0).getPaymentCardId());
		assertEquals("1015", rows.get(0).getExternalReferenceId());
		assertEquals(OperationType.AUTHORIZATION, rows.get(0).getOperationType());
		assertEquals(PaymentsStatus.APPROVED, rows.get(0).getStatus());
		assertEquals(1050L, rows.get(1).getPaymentCardId());
		assertEquals("1015", rows.get(1).getExternalReferenceId());
		assertEquals(OperationType.CAPTURE, rows.get(1).getOperationType());
		assertEquals(PaymentsStatus.APPROVED, rows.get(1).getStatus());
		assertEquals(2, repository.findByExternalReferenceIdAndAcquirerAndPaymentCardId(
				"1015", ACQUIRER, 1050L).size());
	}

	@Test
	void shouldPersistApprovedAuthorizationAndErrorCaptureForSamePaymentCard() {
		List<PaymentOperationEntity> rows = rowsThatWouldBeInserted(
				"caminho infeliz 1",
				operation(1051L, "1016", OperationType.AUTHORIZATION, PaymentsStatus.APPROVED, AT_2130, AT_2133),
				operation(1051L, "1016", OperationType.CAPTURE, PaymentsStatus.ERROR, AT_2133, AT_2133)
		);

		assertEquals(1051L, rows.get(0).getPaymentCardId());
		assertEquals("1016", rows.get(0).getExternalReferenceId());
		assertEquals(OperationType.AUTHORIZATION, rows.get(0).getOperationType());
		assertEquals(PaymentsStatus.APPROVED, rows.get(0).getStatus());
		assertEquals(1051L, rows.get(1).getPaymentCardId());
		assertEquals("1016", rows.get(1).getExternalReferenceId());
		assertEquals(OperationType.CAPTURE, rows.get(1).getOperationType());
		assertEquals(PaymentsStatus.ERROR, rows.get(1).getStatus());
		assertEquals(2, repository.findByExternalReferenceIdAndAcquirerAndPaymentCardId(
				"1016", ACQUIRER, 1051L).size());
	}

	@Test
	void shouldPersistAuthorizationWithError() {
		List<PaymentOperationEntity> rows = rowsThatWouldBeInserted(
				"caminho infeliz 2",
				operation(1052L, "1017", OperationType.AUTHORIZATION, PaymentsStatus.ERROR, AT_2130, AT_2133)
		);

		assertEquals(1052L, rows.get(0).getPaymentCardId());
		assertEquals("1017", rows.get(0).getExternalReferenceId());
		assertEquals(OperationType.AUTHORIZATION, rows.get(0).getOperationType());
		assertEquals(PaymentsStatus.ERROR, rows.get(0).getStatus());
		assertEquals(1, repository.findByExternalReferenceIdAndAcquirerAndPaymentCardId(
				"1017", ACQUIRER, 1052L).size());
	}

	@Test
	void shouldPersistAuthorizationInDoubt() {
		List<PaymentOperationEntity> rows = rowsThatWouldBeInserted(
				"caminho infeliz 3",
				operation(1053L, "1018", OperationType.AUTHORIZATION, PaymentsStatus.IN_DOUBT, AT_2130, AT_2133)
		);

		assertEquals(1053L, rows.get(0).getPaymentCardId());
		assertEquals("1018", rows.get(0).getExternalReferenceId());
		assertEquals(OperationType.AUTHORIZATION, rows.get(0).getOperationType());
		assertEquals(PaymentsStatus.IN_DOUBT, rows.get(0).getStatus());
		assertEquals(1, repository.findByExternalReferenceIdAndAcquirerAndPaymentCardId(
				"1018", ACQUIRER, 1053L).size());
	}

	@Test
	void shouldPersistDeniedAuthorization() {
		List<PaymentOperationEntity> rows = rowsThatWouldBeInserted(
				"caminho infeliz 4",
				operation(1054L, "1019", OperationType.AUTHORIZATION, PaymentsStatus.DENIED, AT_2130, AT_2133)
		);

		assertEquals(1054L, rows.get(0).getPaymentCardId());
		assertEquals("1019", rows.get(0).getExternalReferenceId());
		assertEquals(OperationType.AUTHORIZATION, rows.get(0).getOperationType());
		assertEquals(PaymentsStatus.DENIED, rows.get(0).getStatus());
		assertEquals(1, repository.findByExternalReferenceIdAndAcquirerAndPaymentCardId(
				"1019", ACQUIRER, 1054L).size());
	}

	@Test
	void shouldPersistApprovedAuthorizationAndDeniedCaptureForSamePaymentCard() {
		List<PaymentOperationEntity> rows = rowsThatWouldBeInserted(
				"caminho infeliz 5",
				operation(1055L, "1020", OperationType.AUTHORIZATION, PaymentsStatus.APPROVED, AT_2130, AT_2133),
				operation(1055L, "1020", OperationType.CAPTURE, PaymentsStatus.DENIED, AT_2138, AT_2138)
		);

		assertEquals(1055L, rows.get(0).getPaymentCardId());
		assertEquals("1020", rows.get(0).getExternalReferenceId());
		assertEquals(OperationType.AUTHORIZATION, rows.get(0).getOperationType());
		assertEquals(PaymentsStatus.APPROVED, rows.get(0).getStatus());
		assertEquals(1055L, rows.get(1).getPaymentCardId());
		assertEquals("1020", rows.get(1).getExternalReferenceId());
		assertEquals(OperationType.CAPTURE, rows.get(1).getOperationType());
		assertEquals(PaymentsStatus.DENIED, rows.get(1).getStatus());
		assertEquals(AT_2138, rows.get(1).getCreatedAt());
		assertEquals(2, repository.findByExternalReferenceIdAndAcquirerAndPaymentCardId(
				"1020", ACQUIRER, 1055L).size());
	}

	@Test
	void shouldPersistApprovedAuthorizationAndInDoubtCaptureForSamePaymentCard() {
		List<PaymentOperationEntity> rows = rowsThatWouldBeInserted(
				"caminho infeliz 6",
				operation(1056L, "1021", OperationType.AUTHORIZATION, PaymentsStatus.APPROVED, AT_2130, AT_2133),
				operation(1056L, "1021", OperationType.CAPTURE, PaymentsStatus.IN_DOUBT, AT_2138, AT_2138)
		);

		assertEquals(1056L, rows.get(0).getPaymentCardId());
		assertEquals("1021", rows.get(0).getExternalReferenceId());
		assertEquals(OperationType.AUTHORIZATION, rows.get(0).getOperationType());
		assertEquals(PaymentsStatus.APPROVED, rows.get(0).getStatus());
		assertEquals(1056L, rows.get(1).getPaymentCardId());
		assertEquals("1021", rows.get(1).getExternalReferenceId());
		assertEquals(OperationType.CAPTURE, rows.get(1).getOperationType());
		assertEquals(PaymentsStatus.IN_DOUBT, rows.get(1).getStatus());
		assertEquals(AT_2138, rows.get(1).getCreatedAt());
		assertEquals(2, repository.findByExternalReferenceIdAndAcquirerAndPaymentCardId(
				"1021", ACQUIRER, 1056L).size());
	}

	@Test
	void shouldRejectASecondAuthorizationForTheSamePaymentCard() {
		prepareCard(1050L);
		replaceOperationsForCard(1050L);
		repository.saveAndFlush(mapper.toEntity(
				operation(1050L, "1015", OperationType.AUTHORIZATION, PaymentsStatus.APPROVED, AT_2130, AT_2133)
		));

		assertThrows(DataIntegrityViolationException.class, () -> repository.saveAndFlush(mapper.toEntity(
				operation(1050L, "1015", OperationType.AUTHORIZATION, PaymentsStatus.APPROVED, AT_2133, AT_2133)
		)));
	}

	private PaymentOperation operation(
			Long paymentCardId,
			String externalReferenceId,
			OperationType operationType,
			PaymentsStatus status,
			OffsetDateTime createdAt,
			OffsetDateTime updatedAt) {

		return new PaymentOperation(
				paymentCardId,
				TENANT_ID,
				externalReferenceId,
				ACQUIRER,
				operationType,
				status,
				createdAt,
				updatedAt
		);
	}

	private List<PaymentOperationEntity> rowsThatWouldBeInserted(String scenario, PaymentOperation... operations) {
		assertTrue(operations.length >= 1 && operations.length <= 2);

		List<PaymentOperationEntity> rows = Arrays.stream(operations)
				.map(mapper::toEntity)
				.toList();

		rows.forEach(row -> assertNull(row.getOperationId()));
		Long paymentCardId = rows.get(0).getPaymentCardId();
		prepareCard(paymentCardId);
		replaceOperationsForCard(paymentCardId);

		rows.forEach(repository::saveAndFlush);
		appendLog(scenario, rows, paymentCardId);
		return rows;
	}

	private void prepareCard(Long paymentCardId) {
		entityManager.createNativeQuery("""
				INSERT INTO tenant (id, name, cnpj, status)
				VALUES (:tenantId, 'Tenant teste ST-003', '11111111000111', 'ACTIVE')
				ON CONFLICT (id) DO NOTHING
				""")
				.setParameter("tenantId", TENANT_ID)
				.executeUpdate();

		entityManager.createNativeQuery("""
				INSERT INTO payment_card (
				    payment_card_id, orderpayments_id, tenant_id, cardtoken, brand, instalments, paymentamount, status
				) VALUES (
				    :paymentCardId, 1, :tenantId, :cardToken, 'VISA', 1, 10.00, 'APPROVED'
				)
				ON CONFLICT (payment_card_id) DO NOTHING
				""")
				.setParameter("paymentCardId", paymentCardId)
				.setParameter("tenantId", TENANT_ID)
				.setParameter("cardToken", "token-st003-" + paymentCardId)
				.executeUpdate();
	}

	private void replaceOperationsForCard(Long paymentCardId) {
		entityManager.createNativeQuery("""
				DELETE FROM payment_operation
				WHERE payment_card_id = :paymentCardId
				""")
				.setParameter("paymentCardId", paymentCardId)
				.executeUpdate();
		entityManager.flush();
		entityManager.clear();
	}

	private void appendLog(String scenario, List<PaymentOperationEntity> rows, Long paymentCardId) {
		StringBuilder text = new StringBuilder();
		text.append("cenario=").append(scenario).append(System.lineSeparator());
		text.append("linhas_que_seriam_inseridas=").append(rows.size()).append(System.lineSeparator());
		rows.forEach(row -> text.append(formatRow(row)));

		rows.stream()
				.map(PaymentOperationEntity::getExternalReferenceId)
				.distinct()
				.forEach(reference -> {
					List<PaymentOperationEntity> found = repository.findByExternalReferenceIdAndAcquirerAndPaymentCardId(
							reference,
							ACQUIRER,
							paymentCardId
					);
					text.append("consulta=findByExternalReferenceIdAndAcquirerAndPaymentCardId").append(System.lineSeparator());
					text.append("external_reference_id=").append(reference).append(System.lineSeparator());
					text.append("acquirer=").append(ACQUIRER).append(System.lineSeparator());
					text.append("payment_card_id=").append(paymentCardId).append(System.lineSeparator());
					text.append("lista=").append(found.size()).append(System.lineSeparator());
					found.forEach(row -> text.append(formatRow(row)));
				});

		for (OperationType operationType : OperationType.values()) {
			List<PaymentOperationEntity> found = repository.findByPaymentCardIdAndOperationType(
					paymentCardId,
					operationType
			);
			long expected = rows.stream()
					.filter(row -> row.getOperationType() == operationType)
					.count();
			assertEquals(expected, found.size());
			assertTrue(found.size() <= 1);
			found.forEach(row -> assertEquals(operationType, row.getOperationType()));

			text.append("consulta=findByPaymentCardIdAndOperationType").append(System.lineSeparator());
			text.append("payment_card_id=").append(paymentCardId).append(System.lineSeparator());
			text.append("operation_type=").append(operationType).append(System.lineSeparator());
			text.append("lista=").append(found.size()).append(System.lineSeparator());
			found.forEach(row -> text.append(formatRow(row)));
		}

		text.append(System.lineSeparator());
		String result = text.toString();

		try {
			Files.writeString(
					RESULT_LOG,
					result,
					StandardOpenOption.CREATE,
					StandardOpenOption.APPEND
			);
		} catch (IOException exception) {
			throw new IllegalStateException("Falha ao gravar o log do teste", exception);
		}

		log.info(result);
		System.out.println(result);
	}

	private static String formatRow(PaymentOperationEntity row) {
		return """
				operation_id=%s
				payment_card_id=%s
				tenant_id=%s
				external_reference_id=%s
				acquirer=%s
				operation_type=%s
				status=%s
				created_at=%s
				updated_at=%s
				""".formatted(
				row.getOperationId(),
				row.getPaymentCardId(),
				row.getTenantId(),
				row.getExternalReferenceId(),
				row.getAcquirer(),
				row.getOperationType(),
				row.getStatus(),
				row.getCreatedAt(),
				row.getUpdatedAt()
		);
	}
}
