package com.driven.dm.shop.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ShopErrorCode implements ErrorCode {

    SHOP_NOT_FOUND("SHOP001","해당 가게를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SHOP_NOT_OWNER("SHOP002","가게 관리자만 등록이 가능합니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ShopErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
