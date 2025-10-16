package com.driven.dm.order.domain.policy;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.order.application.exception.OrderErrorCode;
import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.order.domain.entity.OrderStatus;
import com.driven.dm.user.domain.entity.UserRole;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusTransitionPolicy {
    private static final Map<OrderStatus, List<OrderStatus>> ALLOWED = Map.of(
        OrderStatus.PAYMENT_PENDING, List.of(OrderStatus.PAID),
        OrderStatus.PAID,            List.of(OrderStatus.ACCEPTED),
        OrderStatus.ACCEPTED,        List.of(OrderStatus.DELIVERING, OrderStatus.COMPLETED),
        OrderStatus.DELIVERING,      List.of(OrderStatus.COMPLETED)
    );

    private static final List<OrderStatus> CANCEL_ALLOWED = List.of(
        OrderStatus.CREATED, OrderStatus.PAYMENT_PENDING);

    private static final List<OrderStatus> DELETE_ALLOWED = List.of(
        OrderStatus.CREATED, OrderStatus.PAYMENT_PENDING);

    public List<OrderStatus> nextStatuses(OrderStatus current) {
        return ALLOWED.getOrDefault(current, List.of());
    }

    public void assertCanTransition(Order order, UUID actorId, UserRole userRole) {
        if (userRole == UserRole.OWNER && !order.isShopOwner(actorId)) {
            throw AppException.of(OrderErrorCode.NOT_SHOP_OWNER);
        }
        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            throw AppException.of(OrderErrorCode.COMPLETED_ORDER);
        }
    }

    public void assertCanCancel(Order order, UUID actorId, UserRole userRole) {
        if (userRole == UserRole.OWNER && !order.isShopOwner(actorId)) {
            throw AppException.of(OrderErrorCode.NOT_SHOP_OWNER);
        }

        if (!CANCEL_ALLOWED.contains(order.getOrderStatus())) {
            throw AppException.of(OrderErrorCode.INVALID_CANCEL_STATUS);
        }
    }

    public void assertCanDelete(Order order, UUID actorId, UserRole userRole) {
        if (userRole == UserRole.OWNER && !order.isShopOwner(actorId)) {
            throw AppException.of(OrderErrorCode.NOT_SHOP_OWNER);
        }

        if (!DELETE_ALLOWED.contains(order.getOrderStatus())) {
            throw AppException.of(OrderErrorCode.INVALID_CANCEL_STATUS);
        }
    }
}
