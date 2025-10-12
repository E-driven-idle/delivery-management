package com.driven.dm.shop.presentation.dto.request;

import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.domain.entity.ShopStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ShopUpdateDto {

    String shopname;

    String description;

    String shopstatus;

    String shopAddress;

}
