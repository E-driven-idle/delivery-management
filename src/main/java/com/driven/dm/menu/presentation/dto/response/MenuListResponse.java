package com.driven.dm.menu.presentation.dto.response;

import com.driven.dm.menu.domain.entity.Menu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuListResponse {

    private String shopName;
    private String menuName;
    private String menuKeyword;
    private Long menuPrice;

    public static MenuListResponse from(Menu menu){
        MenuListResponse menuListResponse = new MenuListResponse();
       menuListResponse.shopName = menu.getShop().getShopName();
       menuListResponse.menuName = menu.getMenuName();
       menuListResponse.menuKeyword = menu.getMenuKeyword();
       menuListResponse.menuPrice = menu.getMenuPrice();
       return menuListResponse;
    }
}
