package com.driven.dm.cart.application.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CartErrorCode {

    // Cart
    CART_NOT_FOUND("CT001", "장바구니가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND("CT002", "장바구니 항목이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    INVALID_QUANTITY("CT003", "잘못된 수량입니다. 수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),

    // Menu
    MENU_NOT_FOUND("MN001", "메뉴가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MENU_SHOP_MISMATCH("MN002", "메뉴와 가게 정보가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    MENU_SOLD_OUT("MN003", "품절된 상품입니다.", HttpStatus.CONFLICT),        // 재고 부족: 일시적 충돌 → 409
    MENU_SALE_STOPPED("MN004", "판매가 중단된 상품입니다.", HttpStatus.GONE); // 상시 중단(단종) → 410

    private final String code;
    private final String message;
    private final HttpStatus status;

    CartErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
