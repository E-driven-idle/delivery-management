package com.driven.dm.cart.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddItemRequest {

    @NotNull
    private UUID menuId;
    @Positive
    private int quantity;

    @Builder
    private AddItemRequest(UUID menuId, int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static AddItemRequest sample() {
        return AddItemRequest.builder().menuId(UUID.randomUUID()).quantity(2).build();
    }
}
