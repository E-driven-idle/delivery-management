package com.driven.dm.shop.presentation.dto.response;

import com.driven.dm.shop.domain.entity.Shop;

public record ShopCreateResponse(
    String shopName,
    String description,
    String address,
    Double latitude,
    Double longitude
) {
    public static ShopCreateResponse from(Shop shop) {
        return new ShopCreateResponse(
            shop.getShopName(),
            shop.getDescription(),
            shop.getAddress(),
            shop.getLocation().getY(),
            shop.getLocation().getX()
        );
    }

}
