package com.driven.dm.cart.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CartErrorCode implements ErrorCode {

    // 🔹 400 Bad Request
    INVALID_QUANTITY("CART000", "수량은 1개 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_MENU("CART001", "유효하지 않은 메뉴입니다.", HttpStatus.BAD_REQUEST),

    // 🔹 404 Not Found
    CART_NOT_FOUND("CART002", "장바구니를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND("CART003", "장바구니 상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 🔹 409 Conflict
    DUPLICATE_ITEM("CART004", "이미 장바구니에 존재하는 상품입니다.", HttpStatus.CONFLICT),

    // 🔹 403 Forbidden
    CART_ACCESS_DENIED("CART005", "해당 장바구니에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;

    CartErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
