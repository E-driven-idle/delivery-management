package com.driven.dm.payment.presentation.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentStatusResponse {
	private UUID paymentId;
	private String status;
	private String failureReason;
}