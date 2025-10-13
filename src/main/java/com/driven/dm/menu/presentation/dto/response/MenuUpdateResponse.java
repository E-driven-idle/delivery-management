package com.driven.dm.menu.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuUpdateResponse {

    private String menuName;
    private Long menuPrice;

}
