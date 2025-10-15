package com.driven.dm.menu.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuUpdateRequest {

    @Size(max = 10, message = "메뉴명은 최대 10자까지 입력 가능합니다.")
    private String menuName;

    @Size(max = 30, message = "메뉴 소개는 최대 30자까지 입력 가능합니다.")
    private String menuDescription;

    @Pattern(regexp = "^(0|[1-9]\\d*)$", message = "가격은 0이상의 숫자만 입력할 수 있습니다.")
    private String menuPrice;

    @Size(max = 10, message = "키워드는 ")
    private String menuKeyword;

    // open / hidden 만 입력 가능
    private String menuStatus;
}
