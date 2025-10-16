package com.driven.dm.payment.infrastructure.repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.driven.dm.payment.presentation.response.PaymentResponse;

@Component
public class InmemoryIdempotencyStore implements IdempotencyStore {

	private final Map<String, PaymentResponse> cache = new ConcurrentHashMap<>();

	@Override
	public PaymentResponse lookup(UUID userId, String key) {
		return cache.get(userId + ":" + key);
	}

	@Override
	public void store(UUID userId, String key, PaymentResponse response) {
		cache.put(userId + ":" + key, response);
	}
}
