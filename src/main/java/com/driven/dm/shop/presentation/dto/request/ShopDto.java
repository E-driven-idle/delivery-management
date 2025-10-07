package com.driven.dm.shop.presentation.dto.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ShopDto {

    private String shopName;
    private String description;

}
