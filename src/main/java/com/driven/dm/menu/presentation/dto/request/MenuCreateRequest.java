package com.driven.dm.menu.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuCreateRequest {

    private String menuname;
    private Long menuprice;

}
