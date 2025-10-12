package com.driven.dm.cart.presentation.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCartSummaryDto {

    private UUID shopId;
    private String shopName;
    private long cartTotal;
}
