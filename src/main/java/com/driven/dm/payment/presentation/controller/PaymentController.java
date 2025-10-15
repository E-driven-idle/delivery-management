package com.driven.dm.payment.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.payment.application.service.PaymentService;
import com.driven.dm.payment.presentation.request.PaymentCreateRequest;
import com.driven.dm.payment.presentation.response.PaymentResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/api/v1/payments")
	public ResponseEntity<PaymentResponse> createPayment(
		@RequestHeader(value = "Idempotency", required = false) String idemKey,
		@Valid @RequestBody PaymentCreateRequest request,
		@AuthenticationPrincipal SecurityUser securityUser
		) {
		PaymentResponse response = paymentService.createPayment(request, securityUser, idemKey);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
