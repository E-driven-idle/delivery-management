package com.driven.dm.order.presentation.dto.response;

import java.util.UUID;

public record OrderCancelResponse(
    UUID orderId
) {

    public static OrderCancelResponse of(UUID id) {
        return new OrderCancelResponse(id);
    }
}
