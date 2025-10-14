package com.driven.dm.order.application.service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.domain.repository.MenuRepository;
import com.driven.dm.order.application.exception.OrderErrorCode;
import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.order.domain.entity.OrderMenu;
import com.driven.dm.order.infrastructure.repository.OrderRepository;
import com.driven.dm.order.presentation.dto.request.OrderCreateRequest;
import com.driven.dm.order.presentation.dto.request.OrderMenuCreateRequest;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.user.application.service.UserReader;
import com.driven.dm.user.domain.entity.User;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserReader userReader;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;

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

        return order.getId();
    }

    private Shop getOpenedShop(UUID shopId) {
        Shop shop = shopRepository.selectShop(shopId);
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
