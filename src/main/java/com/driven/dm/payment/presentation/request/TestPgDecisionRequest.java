package com.driven.dm.payment.presentation.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TestPgDecisionRequest {

	@NotNull
	private UUID paymentId;

	// 승인 | 거절 | 취소
	@NotNull
	@Pattern(regexp = "approve|decline|cancel")
	private String decision;

	// 실패/취소 사유 (옵션)
	private String reason;
}
