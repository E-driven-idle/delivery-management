package com.driven.dm.cart.presentation.dto.response;

import com.driven.dm.cart.domain.entity.CartItem;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItemResponse {

    private UUID cartItemId;
    private UUID menuId;
    private String menuName;
    private int quantity;
    private long price;
    private long totalPrice;

    public static CartItemResponse from(CartItem item) {
        long unit = item.getUnitPriceSnapshot();
        int qty = item.getQuantity();
        return CartItemResponse.builder()
            .cartItemId(item.getId())
            .menuId(item.getMenu().getId())
            .menuName(item.getMenuNameSnapshot())
            .quantity(qty)
            .price(unit)
            .totalPrice(unit * qty)
            .build();
    }
}
