package com.driven.dm.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ApiErrorCode implements ErrorCode {
    INVALID_REQUEST("API001", "잘못된 요청입니다.", HttpStatus.FORBIDDEN),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    ApiErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
