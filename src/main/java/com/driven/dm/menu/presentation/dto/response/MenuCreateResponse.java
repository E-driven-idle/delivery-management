package com.driven.dm.menu.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuCreateResponse {

    private UUID menuId;
    private String menuName;
    private Long menuPrice;

}
