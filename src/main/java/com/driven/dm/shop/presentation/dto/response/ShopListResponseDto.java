package com.driven.dm.shop.presentation.dto.response;

import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.presentation.dto.response.KakaoAddressSearchResponse.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopListResponseDto {

    private String shopName;
    private String description;
    private double avgRating;
    private ShopAddress address;

}
