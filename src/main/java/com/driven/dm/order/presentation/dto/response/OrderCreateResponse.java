package com.driven.dm.order.presentation.dto.response;

import java.util.UUID;

public record OrderCreateResponse(
    UUID orderId
) {

    public static OrderCreateResponse of(UUID id) {
        return new OrderCreateResponse(id);
    }
}
