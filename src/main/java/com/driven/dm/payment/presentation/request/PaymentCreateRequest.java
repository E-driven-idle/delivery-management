package com.driven.dm.payment.presentation.request;

import java.util.UUID;

import com.driven.dm.payment.domain.entity.PaymentMethod;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentCreateRequest {

	@NotNull
	private UUID orderId;

	@Min(0)
	private Long amount;

	@NotNull
	private PaymentMethod method;

	private boolean approve; // 가짜 PG 승인 여부, 성공 결과를 만들고 싶다면 true, 실패는 false

	@NotNull
	private MethodDetails methodDetails;

	@Getter
	@NoArgsConstructor
	public static class MethodDetails {
		private Card card;
	}

	@Getter
	@NoArgsConstructor
	public static class Card {
		@NotBlank(message = "카드 브랜드(brand)는 필수입니다.")
		private String brand;
		@NotBlank(message = "카드번호 마지막 4자리는 필수입니다.")
		@Size(min = 4, max = 4, message = "카드번호 마지막 4자리는 정확히 4자리여야 합니다.")
		@Pattern(regexp = "\\d{4}", message = "카드번호 마지막 4자리는 숫자만 입력해야 합니다.")
		private String last4;
	}

	@AssertTrue(message = "결제수단과 세부정보가 일치하지 않습니다.")
	private boolean isMethodConsistent() {
		boolean hasCard = methodDetails.getCard() != null;

		return switch (method) {
			case CARD -> hasCard;
		};
	}
}
