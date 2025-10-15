package com.driven.dm.payment.application.service;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.order.application.exception.PaymentErrorCode;
import com.driven.dm.order.application.service.OrderReader;
import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.payment.application.mapper.PaymentRequestMapper;
import com.driven.dm.payment.domain.entity.Payment;
import com.driven.dm.payment.domain.repository.IdempotencyStore;
import com.driven.dm.payment.domain.repository.PaymentRepository;
import com.driven.dm.payment.presentation.request.PaymentCreateRequest;
import com.driven.dm.payment.presentation.response.PaymentResponse;
import com.driven.dm.user.application.service.UserReader;
import com.driven.dm.user.domain.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentRepository paymentRepository;
	private final UserReader userReader;
	private final OrderReader orderReader;
	private final IdempotencyStore idempotencyStore;

	public PaymentResponse createPayment(PaymentCreateRequest request, SecurityUser securityUser, String idemKey) {
		if (idemKey != null) {
			PaymentResponse cached = idempotencyStore.lookup(securityUser.getId(), idemKey);
			if (cached != null) {
				log.info("[Idempotency] 중복 요청 감지 → 캐시된 응답 반환");
				return cached;
			}
		}

		User loginUser = userReader.findActiveUser(securityUser.getId());
		Order order = orderReader.findPendingOrder(request.getOrderId());
		UUID userId = loginUser.getId();

		validateOrder(request, order, userId);

		// 3. 결제 Entity 생성 (INIT)
		Map<String, Object> detailsMap = PaymentRequestMapper.toGenericDetailsMap(request);
		Payment payment = Payment.of(request, loginUser, order, idemKey, detailsMap);

		Payment saved = paymentRepository.save(payment);

		PaymentResponse response = PaymentResponse.from(saved);

		if (idemKey != null) {
			idempotencyStore.store(userId, idemKey, response);
			log.info("[Idempotency] 신규 요청 저장 완료");
		}

		return response;
	}

	private void validateOrder(PaymentCreateRequest request, Order order, UUID userId) {
		// 2) 금액/주문 검증
		if (!Objects.equals(order.getUser().getId(), userId)) {
			throw new AppException(PaymentErrorCode.FORBIDDEN_ORDER);
		}
		if (!Objects.equals(order.getTotalPrice(), request.getAmount())) {
			throw new AppException(PaymentErrorCode.AMOUNT_MISMATCH);
		}
	}
}
