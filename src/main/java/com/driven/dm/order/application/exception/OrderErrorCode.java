package com.driven.dm.order.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode implements ErrorCode {
    SHOP_CLOSED("ORDER001", "주문 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    INVALID_MENU("ORDER002", "주문이 불가능한 메뉴가 있습니다", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY("ORDER003", "주문 수량은 0이하일 수 없습니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;

    OrderErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
