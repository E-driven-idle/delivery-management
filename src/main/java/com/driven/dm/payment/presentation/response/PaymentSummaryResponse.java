package com.driven.dm.payment.presentation.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.driven.dm.payment.domain.entity.Payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentSummaryResponse {

	private UUID paymentId;
	private UUID userId;
	private UUID orderId;
	private Long amount;
	private String status;
	private String failureReason;
	private LocalDateTime createdAt;

	public static PaymentSummaryResponse from(Payment payment) {
		return PaymentSummaryResponse.builder()
									 .paymentId(payment.getId())
									 .userId(payment.getUser().getId())
									 .orderId(payment.getOrder().getId())
									 .amount(payment.getAmount())
									 .status(payment.getStatus().name())
									 .failureReason(
										 payment.getFailureReason() != null ? payment.getFailureReason() : null)
									 .createdAt(payment.getCreatedAt())
									 .build();
	}
}
