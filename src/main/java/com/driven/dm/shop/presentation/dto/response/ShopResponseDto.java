package com.driven.dm.shop.presentation.dto.response;

import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.domain.entity.ShopCategory;
import com.driven.dm.shop.domain.entity.ShopStatus;
import java.util.Optional;
import java.util.UUID;
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
