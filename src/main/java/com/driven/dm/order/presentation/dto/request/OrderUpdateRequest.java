package com.driven.dm.order.presentation.dto.request;

import com.driven.dm.order.domain.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OrderUpdateRequest(
    @NotNull
    UUID orderId,

    @NotNull
    OrderStatus orderStatus
) {

}
