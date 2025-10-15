package com.driven.dm.order.domain.event;

import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.order.domain.entity.OrderStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OrderStatusChangedEvent(
    UUID orderId,
    String orderNo,
    OrderStatus before,
    OrderStatus after,
    String reason
) {

    public static OrderStatusChangedEvent from(Order order, OrderStatus before) {
        return OrderStatusChangedEvent.builder()
            .orderId(order.getId())
            .orderNo(order.getOrderNo())
            .before(before)
            .after(order.getOrderStatus())
            .build();
    }
}
