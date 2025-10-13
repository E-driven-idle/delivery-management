package com.driven.dm.user.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode implements ErrorCode {

    USER_UNAUTHORIZED("USER000", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND("USER001", "해당 고객을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_USER_NAME("USER002", "이미 사용 중인 ID 입니다.", HttpStatus.CONFLICT),
    DUPLICATE_NICK_NAME("USER003", "이미 사용 중인 닉네임입니다.", HttpStatus.CONFLICT),
    USER_FORBIDDEN("USER004", "권한이 없습니다", HttpStatus.FORBIDDEN),
    MAX_ADDRESS_REACHED("USER005", "주소는 최대 10개까지 등록가능합니다.", HttpStatus.CONFLICT),
    SELF_ROLE_UPDATE("USER006", "본인의 권한은 변경할 수 없습니다.", HttpStatus.CONFLICT),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    UserErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
