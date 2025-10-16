package com.driven.dm.payment.application.service;

import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.order.application.exception.PaymentErrorCode;
import com.driven.dm.payment.domain.entity.Payment;
import com.driven.dm.payment.domain.entity.PaymentStatus;
import com.driven.dm.payment.infrastructure.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestPgService {

	private final PaymentRepository paymentRepository;

	// PAYMENT_PENDING 또는 PG_REQUESTED 상태일 때만 최종 결정을 허용
	private static final Set<PaymentStatus> ALLOW_FROM =
		EnumSet.of(PaymentStatus.PAYMENT_PENDING, PaymentStatus.PG_REQUESTED);

	@Transactional
	public Payment decide(UUID paymentId, String decision, String reason) {
		Payment p = paymentRepository.findById(paymentId)
									 .orElseThrow(() -> new AppException(PaymentErrorCode.PAYMENT_NOT_FOUND));

		// 이미 최종 상태면 그대로 반환
		if (!ALLOW_FROM.contains(p.getStatus()))
			return p;

		switch (decision) {
			case "approve" -> p.approve(UUID.randomUUID().toString());
			case "decline" -> p.decline(reason);
			case "cancel" -> p.cancel(reason);
			default -> throw new IllegalArgumentException("unknown decision");
		}
		return p;
	}
}
