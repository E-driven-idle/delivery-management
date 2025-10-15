package com.driven.dm.shop.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShopAddressUpdateRequest {

    @NotBlank(message = "주소 검색어(query)는 필수 입력 항목입니다.")
    private String query;

}
