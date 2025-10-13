package com.driven.dm.cart.presentation.controller;

import com.driven.dm.cart.application.service.CartService;
import com.driven.dm.cart.presentation.dto.request.AddItemRequest;
import com.driven.dm.cart.presentation.dto.request.UpdateQtyRequest;
import com.driven.dm.cart.presentation.dto.response.CartItemResponse;
import com.driven.dm.cart.presentation.dto.response.CartResponse;
import com.driven.dm.cart.presentation.dto.response.UserCartsResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/shops/{shopId}/cart/items")
    public ResponseEntity<CartItemResponse> addItem(
        @PathVariable UUID shopId,
        @Valid @RequestBody AddItemRequest request,
        @AuthenticationPrincipal(expression = "id") UUID userId
    ) {
        CartItemResponse res = cartService.addItem(userId, shopId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/shops/{shopId}/cart")
    public ResponseEntity<CartResponse> getShopCart(
        @PathVariable UUID shopId,
        @AuthenticationPrincipal(expression = "id") UUID userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        CartResponse res = cartService.getShopCart(userId, shopId, page, size);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/carts")
    public ResponseEntity<UserCartsResponse> getUserCarts(
        @AuthenticationPrincipal(expression = "id") UUID userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        UserCartsResponse res = cartService.getUserCarts(userId, page, size);
        return ResponseEntity.ok(res);
    }


    @PatchMapping("/shops/{shopId}/cart/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateQuantity(
        @PathVariable UUID shopId,
        @PathVariable UUID cartItemId,
        @Valid @RequestBody UpdateQtyRequest req,
        @AuthenticationPrincipal(expression = "id") UUID userId
    ) {
        CartItemResponse response =
            cartService.updateQuantity(userId, shopId, cartItemId, req);
        return ResponseEntity.ok(response);
    }


}
