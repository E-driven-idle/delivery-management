package com.driven.dm.payment.presentation.controller;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.payment.application.service.PaymentService;
import com.driven.dm.payment.domain.entity.Payment;
import com.driven.dm.payment.presentation.request.PaymentCreateRequest;
import com.driven.dm.payment.presentation.request.PaymentSearchCond;
import com.driven.dm.payment.presentation.response.PaymentResponse;
import com.driven.dm.payment.presentation.response.PaymentStatusResponse;
import com.driven.dm.payment.presentation.response.PaymentSummaryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(("/api/v1"))
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/payments")
	public ResponseEntity<PaymentResponse> createPayment(
		@RequestHeader(value = "Idempotency", required = false) String idemKey,
		@Valid @RequestBody PaymentCreateRequest request,
		@AuthenticationPrincipal SecurityUser securityUser
	) {
		PaymentResponse response = paymentService.createPayment(request, securityUser, idemKey);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/payments/{paymentId}")
	public ResponseEntity<PaymentStatusResponse> getPaymentStatus(@PathVariable UUID paymentId) {
		PaymentStatusResponse response = paymentService.getPayment(paymentId);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/payments")
	@PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
	public ResponseEntity<Page<PaymentSummaryResponse>> getPayments(
		@ModelAttribute PaymentSearchCond cond,
		@PageableDefault(size = 10, sort = "createdAt", direction = DESC) Pageable pageable
	) {
		Page<Payment> payments = paymentService.searchPayments(cond, pageable);
		System.out.println(cond.getStatus());
		Page<PaymentSummaryResponse> result = payments.map(PaymentSummaryResponse::from);
		System.out.println(result);
		return ResponseEntity.ok(result);
	}
}
