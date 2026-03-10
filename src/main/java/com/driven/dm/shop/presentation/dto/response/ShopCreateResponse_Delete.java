package com.driven.dm.shop.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopCreateResponse_Delete {

    private String shopName;
    private String shopDescription;

}
