package com.driven.dm.shop.presentation.dto.response;

import com.driven.dm.shop.domain.entity.ShopStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AdminShopListResponse {

    private UUID shopId;
    private ShopStatus status;
    private String shopName;
    private String description;
    private String category;
    private double avgRating;
    private String fullAddress;
}
