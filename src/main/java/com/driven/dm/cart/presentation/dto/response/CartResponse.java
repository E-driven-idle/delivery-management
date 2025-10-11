package com.driven.dm.cart.presentation.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartResponse {

    private String shopName;
    private List<ShopCartItemDto> items;
    private long cartTotal;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
}
