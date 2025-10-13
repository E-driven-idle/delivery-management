package com.driven.dm.menu.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MenuErrorCode implements ErrorCode {

    MENU_SAVE_FAIL("MENU001", "메뉴 등록 실패", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    MenuErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
