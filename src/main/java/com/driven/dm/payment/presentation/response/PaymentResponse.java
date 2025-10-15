package com.driven.dm.payment.presentation.response;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import com.driven.dm.payment.domain.entity.Payment;
import com.driven.dm.payment.domain.entity.PaymentMethod;
import com.driven.dm.payment.domain.entity.PaymentStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {

	private UUID paymentId;
	private UUID orderId;
	private PaymentMethod method;
	private PaymentStatus status;
	private String provider;
	private int amount;
	private LocalDateTime approvedAt;
	private LocalDateTime refundedAt;
	private Map<String, Object> summary;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	/* PG 결제창 호출용 토큰 */
	private String clientToken;

	public static PaymentResponse from(Payment payment) {
		if (payment == null) return null;

		return PaymentResponse.builder()
							  .paymentId(payment.getId())
							  .orderId(payment.getOrder() != null ? payment.getOrder().getId() : null)
							  .method(payment.getMethod())
							  .status(payment.getStatus())
							  .provider(payment.getPgProvider())
							  .amount(payment.getAmount() != null ? payment.getAmount().intValue() : 0)
							  .summary(payment.getDetails() != null ? payment.getDetails() : Map.of())
							  .createdAt(payment.getCreatedAt())
							  .updatedAt(payment.getUpdatedAt())
							  .build();
	}
}
