package com.driven.dm.order.application.service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.infrastructure.repository.MenuRepository;
import com.driven.dm.order.application.exception.OrderErrorCode;
import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.order.domain.entity.OrderMenu;
import com.driven.dm.order.domain.entity.OrderStatus;
import com.driven.dm.order.domain.event.OrderStatusChangedEvent;
import com.driven.dm.order.domain.policy.OrderStatusTransitionPolicy;
import com.driven.dm.order.infrastructure.repository.OrderRepository;
import com.driven.dm.order.presentation.dto.request.OrderCreateRequest;
import com.driven.dm.order.presentation.dto.request.OrderMenuCreateRequest;
import com.driven.dm.order.presentation.dto.request.OrderUpdateRequest;
import com.driven.dm.order.presentation.dto.response.OrderPageResponse;
import com.driven.dm.order.presentation.dto.response.OrderResponse;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.user.application.service.UserReader;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.presentation.dto.ApiUser;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserReader userReader;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

    private final OrderStatusTransitionPolicy orderStatusTransitionPolicy;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public UUID createOrder(OrderCreateRequest request) {
        User orderUser = userReader.findActiveUser(request.orderUserId());
        Shop shop = getOpenedShop(request.shopId());

        List<OrderMenuCreateRequest> requestOrderMenu = request.orderMenus();
        Map<UUID, Menu> menuMap = getValidateMenus(shop.getId(), requestOrderMenu);

        List<OrderMenu> orderMenus = getValidateOrderMenus(menuMap, requestOrderMenu);

        Order order = Order.of(
            orderUser,
            shop,
            orderMenus.stream().mapToLong(OrderMenu::getTotalPrice).sum(),
            request.orderType(),
            request.orderRequest()
        );
        orderMenus.forEach(order::addOrderMenu);
        orderRepository.save(order);

        applicationEventPublisher.publishEvent(
            OrderStatusChangedEvent.from(order, OrderStatus.CREATED));
        return order.getId();
    }

    @Transactional
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER', 'OWNER')")
    public OrderResponse updateOrder(ApiUser apiUser, OrderUpdateRequest orderUpdateRequest) {
        Order order = findOrderOrThrow(orderUpdateRequest.orderId());
        orderStatusTransitionPolicy.assertCanTransition(order, apiUser.userId(), apiUser.role());

        List<OrderStatus> canUpdateOrderStatuses = orderStatusTransitionPolicy.nextStatuses(
            order.getOrderStatus());

        OrderStatus targetStatus = orderUpdateRequest.orderStatus();

        if (!canUpdateOrderStatuses.contains(targetStatus)) {
            throw AppException.of(OrderErrorCode.INVALID_UPDATE_STATUS);
        }
        OrderStatus beforeStatus = order.getOrderStatus();
        order.updateStatus(targetStatus);

        applicationEventPublisher.publishEvent(
            OrderStatusChangedEvent.from(order, beforeStatus));
        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public OrderPageResponse getOrders(UUID userId, Long page, Long pageSize) {
        List<Order> orders = orderRepository.findAll(userId, (page - 1) * pageSize, pageSize);

        return OrderPageResponse.of(
            orders.stream()
                .map(OrderResponse::of)
                .toList(),
            orderRepository.count(userId)
        );
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(UUID orderId, UUID userId) {
        Order order = orderRepository.findByIdAndUser_Id(orderId, userId).orElseThrow(() -> {
            throw AppException.of(OrderErrorCode.ORDER_NOT_FOUND);
        });
        return OrderResponse.of(order);
    }

    @Transactional
    public UUID cancelOrder(UUID orderId, ApiUser apiUser) {
        Order order = findOrderOrThrow(orderId);

        orderStatusTransitionPolicy.assertCanCancel(order, apiUser.userId(), apiUser.role());

        order.cancel();
        return order.getId();
    }

    @Transactional
    public UUID deleteOrder(UUID orderId, ApiUser apiUser) {
        Order order = findOrderOrThrow(orderId);

        orderStatusTransitionPolicy.assertCanDelete(order, apiUser.userId(), apiUser.role());

        order.delete(apiUser.userId());
        return order.getId();
    }

    private Order findOrderOrThrow(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> {
            throw AppException.of(OrderErrorCode.ORDER_NOT_FOUND);
        });
    }

    private Shop getOpenedShop(UUID shopId) {
        Shop shop = shopRepository.selectShop(shopId).orElseThrow(() -> {
                throw AppException.of(ShopErrorCode.SHOP_NOT_FOUND);
            });
        if (shop.getStatus() != ShopStatus.OPEN) {
            throw AppException.of(OrderErrorCode.SHOP_CLOSED);
        }
        return shop;
    }

    private Map<UUID, Menu> getValidateMenus(UUID shopId, List<OrderMenuCreateRequest> items) {
        List<UUID> ids = items.stream().map(OrderMenuCreateRequest::menuId).toList();

        List<Menu> menus = menuRepository.findAllByIdInAndShopIdAndDeletedAtIsNull(ids, shopId);
        if (menus.size() != ids.size()) {
            throw AppException.of(OrderErrorCode.INVALID_MENU);
        }
        return menus.stream().collect(Collectors.toMap(Menu::getId, m -> m));
    }

    private List<OrderMenu> getValidateOrderMenus(Map<UUID, Menu> menuMap,
        List<OrderMenuCreateRequest> requestMenus) {

        return requestMenus.stream()
            .map(menu -> {
                if (menu.quantity() <= 0) {
                    throw AppException.of(OrderErrorCode.INVALID_QUANTITY);
                }

                return OrderMenu.of(menuMap.get(menu.menuId()), menu.quantity());
            }).toList();
    }
}
