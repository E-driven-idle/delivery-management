package com.driven.dm.order.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record OrderMenuCreateRequest(
    @NotNull UUID menuId,
    @Min(1) int quantity
) {}
