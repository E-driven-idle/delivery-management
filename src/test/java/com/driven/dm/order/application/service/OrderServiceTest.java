package com.driven.dm.order.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.infrastructure.repository.MenuRepository;
import com.driven.dm.order.application.exception.OrderErrorCode;
import com.driven.dm.order.domain.entity.Order;
import com.driven.dm.order.domain.entity.OrderStatus;
import com.driven.dm.order.domain.entity.OrderType;
import com.driven.dm.order.infrastructure.repository.OrderRepository;
import com.driven.dm.order.presentation.dto.request.OrderCreateRequest;
import com.driven.dm.order.presentation.dto.request.OrderMenuCreateRequest;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.infrastructure.repository.ShopRepository;
import com.driven.dm.user.application.service.UserReader;
import com.driven.dm.user.domain.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    MenuRepository menuRepository;

    @Mock
    ShopRepository shopRepository;

    @Mock
    UserReader userReader;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Nested
    class createOrder {

        private UUID userId;
        private UUID shopId;
        private UUID menuId;
        private OrderCreateRequest request;

        private User user;

        @BeforeEach
        void setup() {
            userId = UUID.randomUUID();
            shopId = UUID.randomUUID();
            menuId = UUID.randomUUID();
            request = OrderCreateRequest.builder()
                .shopId(shopId)
                .orderUserId(userId)
                .orderType(OrderType.DELIVERY)
                .orderMenus(List.of(new OrderMenuCreateRequest(menuId, 2)))
                .orderRequest("문앞에 놓아주세요")
                .build();

            user = User.of("sejun", "1234", "sejunO");
            ReflectionTestUtils.setField(user, "id", userId);

            given(userReader.findActiveUser(userId)).willReturn(user);
        }

        @Test
        @DisplayName("유효한 요청으로 주문을 생성한다")
        void shouldCreateOrder_whenValidRequest() {
            Menu menu = mock(Menu.class);
            given(menu.getId()).willReturn(menuId);
            given(menu.getMenuName()).willReturn("짜장면");
            given(menu.getMenuPrice()).willReturn(8_000L);

            Shop mockShop = mock(Shop.class);
            given(mockShop.getId()).willReturn(shopId);
            given(mockShop.getStatus()).willReturn(ShopStatus.OPEN);

            given(shopRepository.findById(shopId)).willReturn(Optional.of(mockShop));
            given(menuRepository.findAllByIdInAndShopIdAndDeletedAtIsNull(
                request.orderMenus().stream().map(OrderMenuCreateRequest::menuId).toList(),
                shopId)).willReturn(List.of(menu));

            Order mockOrder = mock(Order.class);
            ArgumentCaptor<Order> order = ArgumentCaptor.forClass(Order.class);
            given(orderRepository.save(order.capture())).willReturn(mockOrder);

            UUID result = orderService.createOrder(request);
            Order savedOrder = order.getValue();

            assertThat(savedOrder.getId()).isEqualTo(result);
            assertThat(savedOrder.getUser()).isEqualTo(user);
            assertThat(savedOrder.getShop()).isEqualTo(mockShop);
            assertThat(savedOrder.getOrderNo()).isNotEmpty();
            assertThat(savedOrder.getOrderMenus()).hasSize(1);
            assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.PAYMENT_PENDING);
            assertThat(savedOrder.getTotalPrice()).isEqualTo(16000L);
            assertThat(savedOrder.getOrderRequest()).isEqualTo("문앞에 놓아주세요");
        }

        @Test
        @DisplayName("가게가 주문가능한 상태가 아니면 실패한다")
        void shouldFailToCreateOrder_whenShopIsClosed() {
            Shop mockShop = mock(Shop.class);
            given(mockShop.getStatus()).willReturn(ShopStatus.CLOSED);

            given(shopRepository.findById(shopId)).willReturn(Optional.of(mockShop));

            assertThatThrownBy(() -> {
                orderService.createOrder(request);
            }).isInstanceOf(AppException.class).hasMessage(OrderErrorCode.SHOP_CLOSED.getMessage());
        }

        @Test
        @DisplayName("주문이 불가능한 메뉴가 있으면 실패한다")
        void shouldFailToCreateOrder_whenMenuIsInactive() {
            Shop mockShop = mock(Shop.class);
            given(mockShop.getId()).willReturn(shopId);
            given(mockShop.getStatus()).willReturn(ShopStatus.OPEN);

            given(shopRepository.findById(shopId)).willReturn(Optional.of(mockShop));
            given(menuRepository.findAllByIdInAndShopIdAndDeletedAtIsNull(
                request.orderMenus().stream().map(OrderMenuCreateRequest::menuId).toList(),
                shopId)).willReturn(List.of());

            assertThatThrownBy(() -> {
                orderService.createOrder(request);
            }).isInstanceOf(AppException.class)
                .hasMessage(OrderErrorCode.INVALID_MENU.getMessage());
        }

        @Test
        @DisplayName("주문 수량이 0 이하면 실패한다")
        void shouldFailToCreateOrder_whenQuantityIsNonPositive() {
            request = OrderCreateRequest.builder()
                .shopId(shopId)
                .orderUserId(userId)
                .orderType(OrderType.DELIVERY)
                .orderMenus(List.of(new OrderMenuCreateRequest(menuId, 0)))
                .orderRequest("문앞에 놓아주세요")
                .build();

            Menu menu = mock(Menu.class);
            Shop mockShop = mock(Shop.class);

            given(mockShop.getId()).willReturn(shopId);
            given(mockShop.getStatus()).willReturn(ShopStatus.OPEN);
            given(shopRepository.findById(shopId)).willReturn(Optional.of(mockShop));
            given(menuRepository.findAllByIdInAndShopIdAndDeletedAtIsNull(
                request.orderMenus().stream().map(OrderMenuCreateRequest::menuId).toList(),
                shopId)).willReturn(List.of(menu));

            assertThatThrownBy(() -> {
                orderService.createOrder(request);
            }).isInstanceOf(AppException.class)
                .hasMessage(OrderErrorCode.INVALID_QUANTITY.getMessage());
        }
    }

}