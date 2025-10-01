package com.driven.dm.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException{

    private final HttpStatus status;

    public AppException(ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }

    protected AppException(HttpStatus httpStatus, String message) {
        super(message);
        this.status = httpStatus;
    }

    public static AppException of(ErrorCode errorCode) {
        return new AppException(errorCode);
    }
}
