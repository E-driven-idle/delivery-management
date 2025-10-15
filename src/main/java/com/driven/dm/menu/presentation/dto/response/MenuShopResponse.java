package com.driven.dm.menu.presentation.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuShopResponse {

    private String shopName;
    private List<MenuResponse> menus;

    @Getter
    @Builder
    public static class MenuResponse {
        private String menuName;
        private String menuDescription;
        private String menuKeyword;
        private Long menuPrice;
    }

}
