package com.driven.dm.shop.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShopCreateRequest {

    @NotBlank(message = "가게 이름은 필수입니다.")
    @Size(max = 50, message = "가게 이름은 최대50자 이내여야 합니다.")
    private String shopName;

    @Size(max = 200, message = "가게 소개는 200자 이내여야 합니다.")
    private String description;

}
