package com.driven.dm.order.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode implements ErrorCode {
    SHOP_CLOSED("ORDER001", "주문 가능한 상태가 아닙니다.", HttpStatus.CONFLICT),
    INVALID_MENU("ORDER002", "주문이 불가능한 메뉴가 있습니다", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY("ORDER003", "주문 수량은 0이하일 수 없습니다.", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("ORDER004", "해당 주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    NOT_SHOP_OWNER("ORDER005", "가게 주인이 아닙니다.", HttpStatus.FORBIDDEN),
    COMPLETED_ORDER("ORDER006", "이미 완료된 주문입니다.", HttpStatus.BAD_REQUEST),
    INVALID_UPDATE_STATUS("ORDER007", "변경할 수 없는 상태입니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;

    OrderErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
