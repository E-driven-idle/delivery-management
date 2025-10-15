package com.driven.dm.shop.presentation.dto.response;

import static com.driven.dm.global.util.NumberUtils.round1;

import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopCategory;
import com.driven.dm.shop.domain.entity.ShopStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopResponseDto {

    private String shopName;

    private String description;

    private ShopCategory category;

    private Double avgRating;

    private ShopStatus shopStatus;

    private String fullAddress;

    public static ShopResponseDto from(Shop shop) {
        return ShopResponseDto.builder()
            .shopName(shop.getShopName())
            .description(shop.getDescription())
            .category(shop.getCategory())
            .avgRating(round1(shop.getAvgRating()))
            .shopStatus(shop.getStatus())
            .fullAddress(shop.getAddress() != null ? shop.getAddress().getFullAddress() : null)
            .build();
    }
}

