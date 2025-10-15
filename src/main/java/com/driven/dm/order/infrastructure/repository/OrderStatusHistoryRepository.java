package com.driven.dm.order.infrastructure.repository;

import com.driven.dm.order.domain.entity.OrderStatusHistory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistory, UUID> {

}
