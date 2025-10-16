package com.driven.dm.payment.presentation.response;

import java.util.UUID;

import com.driven.dm.payment.domain.entity.Payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentStatusResponse {
	private UUID paymentId;
	private String status;
	private String failureReason;

	public static PaymentStatusResponse from(Payment payment) {
		if (payment == null) {
			throw new NullPointerException("결제 정보가 null입니다.");
		}

		return PaymentStatusResponse.builder()
									.paymentId(payment.getId())
									.status(payment.getStatus() != null ? payment.getStatus().name() : null)
									.failureReason(payment.getFailureReason()) // null 허용 (REJECTED 아닐 경우)
									.build();
	}

}