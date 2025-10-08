package com.driven.dm.cart.presentation.controller;

import com.driven.dm.cart.application.service.CartService;
import com.driven.dm.cart.presentation.dto.request.AddItemRequest;
import com.driven.dm.cart.presentation.dto.response.CartItemResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shops/{shopId}/cart/items")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /** 장바구니 상품 추가 */
    @PostMapping
    public ResponseEntity<CartItemResponse> addItem(
        @PathVariable UUID shopId,
        @Valid @RequestBody AddItemRequest request,
        @AuthenticationPrincipal(expression = "id") UUID userId   // SecurityUser.getId()
    ) {
        CartItemResponse res = cartService.addItem(userId, shopId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
