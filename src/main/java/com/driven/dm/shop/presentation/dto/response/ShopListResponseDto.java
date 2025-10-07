package com.driven.dm.shop.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopListResponseDto {

    private String shopName;
    private String description;
    private double avgRating;

}
