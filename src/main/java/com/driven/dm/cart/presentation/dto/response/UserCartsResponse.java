package com.driven.dm.cart.presentation.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class UserCartsResponse {

    private UUID userId;
    private List<UserCartSummaryDto> carts;
    private long grandTotal;
    private int page;
    private int size;
    private long totalShops;
    private int totalPages;
}
