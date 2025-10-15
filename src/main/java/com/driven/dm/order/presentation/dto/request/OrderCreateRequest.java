package com.driven.dm.order.presentation.dto.request;

import com.driven.dm.order.domain.entity.OrderType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OrderCreateRequest(
    @NotNull UUID shopId,
    @NotNull UUID orderUserId,
    @NotNull OrderType orderType,
    @Size(max = 500) String orderRequest,
    @NotEmpty List<OrderMenuCreateRequest> orderMenus
) {

}