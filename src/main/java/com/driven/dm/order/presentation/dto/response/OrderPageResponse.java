package com.driven.dm.order.presentation.dto.response;

import java.util.List;

public record OrderPageResponse(
    List<OrderResponse> orders,
    Long count
) {

    public static OrderPageResponse of(List<OrderResponse> orders, Long count) {
        return new OrderPageResponse(orders, count);
    }
}
