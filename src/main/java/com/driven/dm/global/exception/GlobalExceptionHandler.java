package com.driven.dm.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleException(AppException exception) {
        return ResponseEntity.status(exception.getStatus())
            .body(ErrorResponse.from(exception));
    }
}
