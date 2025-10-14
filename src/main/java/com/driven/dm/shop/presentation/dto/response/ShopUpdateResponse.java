package com.driven.dm.shop.presentation.dto.response;

import com.driven.dm.shop.domain.entity.ShopStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShopUpdateResponse {

    private String shopName;
    private String description;
    private ShopStatus shopStatus;

}
