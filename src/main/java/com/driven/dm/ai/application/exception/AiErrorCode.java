package com.driven.dm.ai.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AiErrorCode implements ErrorCode {

    AI_LOG_NOT_FOUND("AI000", "로그를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AI_LOG_ALREADY_DELETED("AI002", "삭제된 로그입니다.", HttpStatus.NOT_FOUND),
    AI_LOG_HAS_NOT_BEEN_DELETED("AI003", "삭제되지 않은 로그입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
