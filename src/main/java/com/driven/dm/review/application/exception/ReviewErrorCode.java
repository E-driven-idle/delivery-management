package com.driven.dm.review.application.exception;

import com.driven.dm.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    // 400 BAD REQUEST
    INVALID_RATING_VALUE("REV-400-01", "평점은 1~5 사이여야 합니다.", HttpStatus.BAD_REQUEST),

    // 403 FORBIDDEN
    FORBIDDEN_REVIEW_OWNER("REV-403-01", "리뷰 수정/삭제 권한이 없습니다.", HttpStatus.FORBIDDEN),

    // 404 NOT FOUND
    REVIEW_NOT_FOUND("REV-404-01", "리뷰를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    REVIEW_IMAGE_NOT_FOUND("REV-404-02", "리뷰 이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 409 CONFLICT
    DUPLICATE_REVIEW("REV-409-01", "이미 작성된 리뷰가 존재합니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
