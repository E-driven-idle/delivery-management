package com.driven.dm.shop.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ShopErrorCode implements ErrorCode {

    SHOP_NOT_FOUND("SHOP001","해당 가게를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    SHOP_NOT_OWNER("SHOP002","가게 관리자만 등록이 가능합니다.", HttpStatus.FORBIDDEN),
    ADDRESS_NO_SUCH("SHOP005", "주소 검색 결과가 없습니다.", HttpStatus.BAD_REQUEST),
    ADDRESS_NO_STATE("SHOP006", "유효한 주소 필드가 없습니다.", HttpStatus.BAD_REQUEST),
    ADDRESS_DUPLICATE("SHOP007", "가게 주소가 이미 등록되어있습니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ShopErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

}
