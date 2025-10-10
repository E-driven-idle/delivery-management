package com.driven.dm.cart.presentation.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartResponse {

    private String shopName;                 // 필요 없으면 제거해도 됨 (지금은 미사용)
    private List<ShopCartItemDto> items;     // 아이템 목록 (페이지 일부)
    private long cartTotal;                  // 해당 가게 전체 합계(페이지 합계 아님)
    private int page;                        // 0-base
    private int size;
    private long totalItems;                 // 전체 아이템 수
    private int totalPages;
}
