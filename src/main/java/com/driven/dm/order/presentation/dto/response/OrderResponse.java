package com.driven.dm.order.presentation.dto.response;

import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.order.domain.entity.OrderStatus;
import com.driven.dm.payment.domain.entity.PaymentStatus;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OrderResponse(
    UUID orderId,
    String orderNo,
    PaymentStatus paymentStatus,
    OrderStatus orderStatus,
    Long totalPrice,
    List<OrderMenuResponse> orderMenus,
    String orderRequest
) {
    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
            .orderId(order.getId())
            .orderNo(order.getOrderNo())
            .paymentStatus(order.getPaymentStatus())
            .orderStatus(order.getOrderStatus())
            .totalPrice(order.getTotalPrice())
            .orderMenus(order.getOrderMenus().stream().map(OrderMenuResponse::of).toList())
            .orderRequest(order.getOrderRequest())
            .build();
    }
}
