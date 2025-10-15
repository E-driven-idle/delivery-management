package com.driven.dm.order.domain.entity;

import com.driven.dm.payment.domain.entity.PaymentStatus;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.user.domain.entity.User;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", nullable = false, updatable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> orderMenus = new ArrayList<>();

    @Column(name = "order_no")
    private String orderNo;
    @Column(name = "total_price")
    private Long totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type")
    private OrderType orderType;
    @Column(name = "order_request")
    private String orderRequest;

    public static Order of(User orderUser, Shop shop, Long totalPrice, OrderType orderType,
        String orderRequest) {
        Order order = new Order();
        order.user = orderUser;
        order.shop = shop;
        order.orderNo = UUID.randomUUID().toString();
        order.totalPrice = totalPrice;
        order.orderStatus = OrderStatus.PAYMENT_PENDING;
        order.paymentStatus = PaymentStatus.PAYMENT_PENDING;
        order.orderType = orderType;
        order.orderRequest = orderRequest;
        return order;
    }

    public void addOrderMenu(OrderMenu orderMenu) {
        orderMenus.add(orderMenu);
        orderMenu.setOrder(this);
    }

    public boolean isShopOwner(UUID userId) {
        return this.getShop().getOwner().getId().equals(userId);
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
