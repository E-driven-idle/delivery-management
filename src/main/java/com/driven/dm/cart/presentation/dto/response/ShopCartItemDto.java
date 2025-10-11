package com.driven.dm.cart.presentation.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShopCartItemDto {

    private UUID cartItemId;
    private UUID menuId;
    private String menuName;
    private int quantity;
    private long price;
    private long totalPrice;
}
