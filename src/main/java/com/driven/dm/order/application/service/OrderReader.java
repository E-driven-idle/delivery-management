package com.driven.dm.order.application.service;

import com.driven.dm.order.infrastructure.repository.OrderRepository;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.order.application.exception.PaymentErrorCode;
import com.driven.dm.order.domain.entity.Order;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderReader {

	private final OrderRepository orderRepository;

	public Order findPendingOrder(UUID orderId) {
		return orderRepository.findById(orderId)
							  .orElseThrow(() -> AppException.of(PaymentErrorCode.PAYMENT_NOT_FOUND));
		// TODO: Order not Found로 바꿔야함. 뭔가 세준님이 만들것 같아서 충돌날까봐 이렇게 함!
	}

}
