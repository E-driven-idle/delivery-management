package com.driven.dm.order.application.exception;

import org.springframework.http.HttpStatus;

import com.driven.dm.global.exception.ErrorCode;

import lombok.Getter;

@Getter
public enum PaymentErrorCode implements ErrorCode {
	PAYMENT_NOT_FOUND("PAYMENT000", "해당 결제 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	FORBIDDEN_ORDER("PAYMENT001", "해당 주문에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
	AMOUNT_MISMATCH("PAYMENT002", "주문 금액과 요청 금액이 일치하지 않습니다.", HttpStatus.CONFLICT),;

	private final String code;
	private final String message;
	private final HttpStatus status;

	PaymentErrorCode(String code, String message, HttpStatus status) {
		this.code = code;
		this.message = message;
		this.status = status;
	}
}
