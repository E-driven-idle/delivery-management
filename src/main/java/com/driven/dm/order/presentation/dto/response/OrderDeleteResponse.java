package com.driven.dm.order.presentation.dto.response;

import java.util.UUID;

public record OrderDeleteResponse(
    UUID orderId
) {

    public static OrderDeleteResponse of(UUID id) {
        return new OrderDeleteResponse(id);
    }
}
