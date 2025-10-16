package com.driven.dm.payment.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driven.dm.payment.application.service.TestPgService;
import com.driven.dm.payment.presentation.request.TestPgDecisionRequest;
import com.driven.dm.payment.presentation.response.PaymentStatusResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/test-pg")
@RequiredArgsConstructor
public class TestPgController {

	private final TestPgService testPgService;

	@PostMapping("/decide")
	public ResponseEntity<PaymentStatusResponse> decide(@RequestBody @Validated TestPgDecisionRequest req) {
		var p = testPgService.decide(req.getPaymentId(), req.getDecision(), req.getReason());
		return ResponseEntity.ok(
			PaymentStatusResponse.builder()
								 .paymentId(p.getId())
								 .status(p.getStatus().name())
								 .failureReason(p.getFailureReason())
								 .build()
		);
	}
}
