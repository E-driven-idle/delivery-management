package com.driven.dm.shop.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShopDto {

    @NotBlank(message = "[가게 등록 실패] : 가게 이름은 필수입니다.")
    @Size(min = 2, max = 10, message = "[가게 등록 실패] : 최소 2글자 최대 10글자 지켜주세요!")
    private String shopname;

    @Size(max = 100, message = "[가게 등록 실패] : 가게 설명 100글자 내외로 작성해주세요!")
    private String description;

}
