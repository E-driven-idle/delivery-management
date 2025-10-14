package com.driven.dm.menu.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuUpdateRequest {

    private String menuName;
    private Long menuPrice;
    private String menuStatus;

}
