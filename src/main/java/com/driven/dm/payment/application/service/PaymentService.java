package com.driven.dm.payment.application.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.order.application.exception.PaymentErrorCode;
import com.driven.dm.order.application.service.OrderReader;
import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.payment.application.mapper.PaymentRequestMapper;
import com.driven.dm.payment.domain.entity.Payment;
import com.driven.dm.payment.infrastructure.repository.IdempotencyStore;
import com.driven.dm.payment.infrastructure.repository.PaymentRepository;
import com.driven.dm.payment.presentation.request.PaymentCreateRequest;
import com.driven.dm.payment.presentation.request.PaymentSearchCond;
import com.driven.dm.payment.presentation.response.PaymentResponse;
import com.driven.dm.payment.presentation.response.PaymentStatusResponse;
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

	public PaymentStatusResponse getPayment(UUID paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
										   .orElseThrow(() -> new AppException(PaymentErrorCode.PAYMENT_NOT_FOUND));

		return PaymentStatusResponse.from(payment);
	}

	public Page<Payment> searchPayments(PaymentSearchCond cond, Pageable pageable) {

		LocalDateTime from = cond.getFromDate() != null ? cond.getFromDate() : LocalDateTime.of(1970, 1, 1, 0, 0, 0);
		LocalDateTime to = cond.getToDate() != null ? cond.getToDate() : LocalDateTime.of(9999, 12, 31, 23, 59, 59);

		if (from.isAfter(to)) {
			LocalDateTime tmp = from;
			from = to;
			to = tmp;
		}

		return paymentRepository.search(cond.getUserId(), cond.getStatus(), from, to, pageable);
	}
}
