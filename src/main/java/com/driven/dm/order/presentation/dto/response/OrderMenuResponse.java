package com.driven.dm.order.presentation.dto.response;

import com.driven.dm.order.domain.entity.OrderMenu;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OrderMenuResponse(
    UUID menuId,
    String menuName,
    Long totalPrice,
    int quantity
) {
    public static OrderMenuResponse of(OrderMenu orderMenu) {
        return OrderMenuResponse.builder()
            .menuId(orderMenu.getId())
            .menuName(orderMenu.getMenuNameSnapshot())
            .quantity(orderMenu.getQuantity())
            .totalPrice(orderMenu.getTotalPrice())
            .build();
    }
}
