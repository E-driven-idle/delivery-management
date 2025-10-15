package com.driven.dm.order.domain.entity;

import com.driven.dm.global.entity.HistoryBaseEntity;
import com.driven.dm.order.domain.event.OrderStatusChangedEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "p_order_status_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderStatusHistory extends HistoryBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "order_no")
    private String orderNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "before_status")
    private OrderStatus beforeStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "after_status")
    private OrderStatus afterStatus;

    @Column(name = "reason")
    private String reason;

    public static OrderStatusHistory of(OrderStatusChangedEvent event) {
        OrderStatusHistory orderStatusHistory = new OrderStatusHistory();
        orderStatusHistory.orderId = event.orderId();
        orderStatusHistory.orderNo = event.orderNo();
        orderStatusHistory.beforeStatus = event.before();
        orderStatusHistory.afterStatus = event.after();
        orderStatusHistory.reason = event.reason();

        return orderStatusHistory;
    }
}
