package com.driven.dm.payment.infrastructure.repository;

import java.util.UUID;

import com.driven.dm.payment.presentation.response.PaymentResponse;

public interface IdempotencyStore {
	PaymentResponse lookup(UUID userId, String key);

	void store(UUID userId, String key, PaymentResponse response);
}
