package com.driven.dm.order.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.driven.dm.order.domain.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
