package com.driven.dm.menu.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuCreateRequest {

    @NotBlank(message = "메뉴명은 필수입니다.")
    @Size(max = 10, message = "메뉴명은 최대 10자까지 입력 가능합니다.")
    private String menuName;

    @Size(max = 30, message = "메뉴 소개는 최대 30자까지 입력 가능합니다.")
    private String menuDescription;

    @NotNull(message = "가격은 필수입니다.")
    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    private Long menuPrice;

    @NotBlank(message = "키워드명은 필수입니다.")
    @Size(max = 10, message = "키워드는 최대 10자까지 입력 가능합니다.")
    private String keyword;

    @Size(max = 50, message = "재료는 최대 50자까지 입력 가능합니다.")
    private String features;

    private boolean generateDescription;

}
