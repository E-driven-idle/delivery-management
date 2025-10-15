package com.driven.dm.order.infrastructure.repository;

import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.order.presentation.dto.response.OrderResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query(
        value = "select p_order.* "
            + "from ("
            + " select order_id, created_at "
            + " from p_order "
            + " where user_id = :userId "
            + " order by created_at desc "
            + " offset :offset limit :limit"
            + ") t left join p_order on t.order_id = p_order.order_id",
        nativeQuery = true
    )
    List<Order> findAll(UUID userId, Long offset, Long limit);

    @Query(
        value = "select count(*) "
            + "from ("
            + " select order_id "
            + " from p_order "
            + " where user_id = :userId"
            + ") t",
        nativeQuery = true
    )
    Long count(UUID userId);

    Optional<Order> findByIdAndUser_Id(UUID orderId, UUID userId);
}
