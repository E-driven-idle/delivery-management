package com.driven.dm.order.presentation.controller;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.order.application.service.OrderService;
import com.driven.dm.order.presentation.dto.request.OrderCreateRequest;
import com.driven.dm.order.presentation.dto.request.OrderUpdateRequest;
import com.driven.dm.order.presentation.dto.response.OrderCreateResponse;
import com.driven.dm.order.presentation.dto.response.OrderPageResponse;
import com.driven.dm.order.presentation.dto.response.OrderResponse;
import com.driven.dm.user.presentation.dto.ApiUser;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders")
    public ResponseEntity<OrderCreateResponse> createOrder(
        @Valid @RequestBody OrderCreateRequest request) {

        UUID orderId = orderService.createOrder(request);
        return ResponseEntity.created(
            UriComponentsBuilder.fromUriString("/api/v1/orders/{orderId}")
                .buildAndExpand(orderId)
                .toUri()
        ).body(OrderCreateResponse.of(orderId));
    }

    @PutMapping("/api/v1/orders")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER', 'OWNER')")
    public ResponseEntity<OrderResponse> updateOrder(
        @RequestBody @Valid OrderUpdateRequest orderUpdateRequest,
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        OrderResponse response = orderService.updateOrder(
            ApiUser.of(securityUser.getId(), securityUser.getRole()),
            orderUpdateRequest
        );
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/v1/orders")
    public ResponseEntity<OrderPageResponse> getOrders(
        @RequestParam("page") Long page,
        @RequestParam("pageSize") Long pageSize,
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        OrderPageResponse response = orderService.getOrders(securityUser.getId(), page, pageSize);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/v1/orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
        @PathVariable("orderId") UUID orderId,
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        OrderResponse response = orderService.getOrder(orderId, securityUser.getId());
        return ResponseEntity.ok().body(response);
    }
}
