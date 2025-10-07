package com.driven.dm.shop.presentation.dto.response;

import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.domain.entity.ShopStatus;
import java.util.Optional;
import java.util.UUID;
import lombok.Builder;

@Builder
public class ShopResponseDto {

    private UUID shopId;

    private String shopName;

    private String description;

    private Double avgRating;

    private ShopStatus shopStatus;

    private ShopAddress address;

    public static ShopResponseDto from(Shop shop) {

        return ShopResponseDto.builder()
            .shopId(shop.getId())
            .shopName(shop.getShopName())
            .description(shop.getDescription())
            .avgRating(shop.getAvgRating())
            .shopStatus(shop.getStatus())
            .address(shop.getAddress())
            .build();
    }

}
