package com.driven.dm.payment.domain.entity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.driven.dm.global.entity.BaseEntity;
import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.payment.presentation.request.PaymentCreateRequest;
import com.driven.dm.user.domain.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "p_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "payment_id", nullable = false, updatable = false)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private PaymentStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "method")
	private PaymentMethod method;

	@Column(name = "amount")
	private Long amount;

	@Column(name = "idemKey")
	private String idemKey;

	@Column(name = "pgProvider")
	private String pgProvider;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "failure_reason", length = 200)
	private String failureReason;  // 실패/취소 사유(DECLINED/CANCELED 시)

	@Column(name = "approved_at")
	private LocalDateTime approvedAt;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "details", columnDefinition = "jsonb", nullable = false)
	private Map<String, Object> details;

	public static Payment of(PaymentCreateRequest request, User loginUser, Order order, String idemKey,
							 Map<String, Object> details) {
		Payment payment = new Payment();
		payment.user = loginUser;
		payment.order = order;
		payment.status = PaymentStatus.PAYMENT_PENDING;
		payment.method = request.getMethod();
		payment.amount = request.getAmount();
		payment.idemKey = idemKey;
		payment.pgProvider = "inhouse-mock"; // 임시 제공자
		payment.details = details;
		return payment;
	}

	public void approve(String pgTid) {
		this.status = PaymentStatus.PAYMENT_APPROVED;
		this.transactionId = pgTid; // 테스트라서 null. 원래는 id값을 넣어줘야함.
		this.approvedAt = LocalDateTime.now();
	}

	public void decline(String reason) {
		this.status = PaymentStatus.PAYMENT_DECLINED;
		this.failureReason = reason;
	}

	public void cancel(String reason) {
		this.status = PaymentStatus.PAYMENT_CANCELED;
		this.failureReason = reason;
	}
}
