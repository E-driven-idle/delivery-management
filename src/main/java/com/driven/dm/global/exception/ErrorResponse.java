package com.driven.dm.global.exception;

public record ErrorResponse(
    String code,
    String message
) {

    public static ErrorResponse from(AppException appException) {
        return new ErrorResponse(appException.getCode(), appException.getMessage());
    }

    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}
