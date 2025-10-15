package com.driven.dm.shop.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ShopUpdateRequest {

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Size(max = 50, message = "가게 이름은 50자 이내여야 합니다.")
    private String shopName;

    @Size(max = 200, message = "가게 소개는 200자 이내여야 합니다.")
    private String description;

    @NotBlank(message = "가게 상태는 필수입니다.")
    @Pattern(regexp = "OPEN|CLOSED", message = "가게 상태는 OPEN 또는 CLOSED만 가능합니다.")
    private String status;

    @Pattern(regexp = "KOREAN|CHINESE|SNACK|CHICKEN|PIZZA",
        message = "카테고리는 KOREAN, CHINESE, SNACK, CHICKEN, PIZZA만 가능합니다.")
    private String category;
}
