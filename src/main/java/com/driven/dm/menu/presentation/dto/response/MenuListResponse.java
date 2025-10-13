package com.driven.dm.menu.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuListResponse {

    private String shopName;
    private String menuName;
    private Long menuPrice;

}
