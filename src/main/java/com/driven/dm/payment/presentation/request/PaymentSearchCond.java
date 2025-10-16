package com.driven.dm.payment.presentation.request;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.driven.dm.payment.domain.entity.PaymentStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PaymentSearchCond {
	private UUID userId;
	private PaymentStatus status;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDateTime fromDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDateTime toDate;
}