package com.driven.dm.menu.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MenuErrorCode implements ErrorCode {

    MENU_NOT_FOUND("MENU000", "요청한 메뉴가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MENU_SAVE_FAIL("MENU001", "등록하려는 메뉴가 등록되지 않았습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    MenuErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
