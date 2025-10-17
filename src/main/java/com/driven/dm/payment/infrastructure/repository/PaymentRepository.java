package com.driven.dm.payment.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.driven.dm.payment.domain.entity.Payment;
import com.driven.dm.payment.domain.entity.PaymentStatus;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

	@Query("""
			SELECT p FROM Payment p
			WHERE (:userId IS NULL OR p.user.id = :userId)
			AND (:status IS NULL OR p.status = :status)
			AND p.createdAt BETWEEN :from AND :to
		""")
	Page<Payment> search(
		@Param("userId") UUID userId,
		@Param("status") PaymentStatus status,
		@Param("from") LocalDateTime from,
		@Param("to") LocalDateTime to,
		Pageable pageable
	);
}
