package com.driven.dm.shop.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopListResponse {

    private String shopName;
    private String description;
    private String category;
    private double avgRating;
    private String fullAddress;

}
