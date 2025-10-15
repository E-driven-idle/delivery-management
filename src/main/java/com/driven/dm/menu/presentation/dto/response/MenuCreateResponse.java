package com.driven.dm.menu.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuCreateResponse {

    private String menuName;
    private String menuDescription;
    private String menuKeyword;
    private Long menuPrice;

}
