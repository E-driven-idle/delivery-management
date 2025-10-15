package com.driven.dm.shop.presentation.dto.response;

import com.driven.dm.shop.domain.entity.Shop;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopCreateResponse {

    private String shopName;
    private String shopDescription;

}
