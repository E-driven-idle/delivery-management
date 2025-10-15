package com.driven.dm.payment.application.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import com.driven.dm.payment.domain.entity.PaymentMethod;
import com.driven.dm.payment.presentation.request.PaymentCreateRequest;

public class PaymentRequestMapper {
	private PaymentRequestMapper() {}

	public static Map<String, Object> toGenericDetailsMap(PaymentCreateRequest req) {
		Map<String, Object> details = new LinkedHashMap<>();
		PaymentCreateRequest.MethodDetails md = req.getMethodDetails();
		if (md == null) return details;

		// 일단 카드만 들어온다는 가정하에 카드만 매핑
		if (req.getMethod() == PaymentMethod.CARD && md.getCard() != null) {
			details.put("type", "card");
			details.put("brand", md.getCard().getBrand());
			details.put("last4", md.getCard().getLast4());
		}

		return details;
	}
}
