package com.driven.dm.shop.presentation.dto.response;

import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopCategory;
import com.driven.dm.shop.domain.entity.ShopStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopResponse {

    private String shopName;

    private String description;

    private ShopCategory category;

    private Double avgRating;

    private ShopStatus shopStatus;

    private String fullAddress;

    public static ShopResponse from(Shop shop) {

        return ShopResponse.builder()
            .shopName(shop.getShopName())
            .description(shop.getDescription())
            .category(shop.getCategory())
            .avgRating(shop.getAvgRating())
            .shopStatus(shop.getStatus())
            .fullAddress(
                shop.getAddress().getFullAddress() != null
                    ? shop.getAddress().getFullAddress()
                    : null
            )
            .build();
    }

}
