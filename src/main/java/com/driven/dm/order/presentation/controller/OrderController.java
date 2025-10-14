package com.driven.dm.order.presentation.controller;

import com.driven.dm.order.application.service.OrderService;
import com.driven.dm.order.presentation.dto.request.OrderCreateRequest;
import com.driven.dm.order.presentation.dto.response.OrderCreateResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
