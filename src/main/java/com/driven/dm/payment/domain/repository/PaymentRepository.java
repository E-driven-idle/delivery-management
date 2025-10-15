package com.driven.dm.payment.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driven.dm.payment.domain.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
