package com.driven.dm.cart.domain.entity;

import com.driven.dm.global.entity.BaseEntity;
import com.driven.dm.menu.domain.entity.Menu;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_cart_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_item_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "menu_name", nullable = false, length = 100)
    private String menuNameSnapshot;

    @Column(name = "unit_price", nullable = false)
    private Long unitPriceSnapshot;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    private CartItem(Cart cart, Menu menu, String menuNameSnapshot, Long unitPriceSnapshot) {
        this.cart = cart;
        this.menu = menu;
        this.menuNameSnapshot = menuNameSnapshot;
        this.unitPriceSnapshot = unitPriceSnapshot;
    }

    public static CartItem of(Cart cart, Menu menu, String menuNameSnapshot,
        Long unitPriceSnapshot) {
        return new CartItem(cart, menu, menuNameSnapshot, unitPriceSnapshot);
    }

    public void increase(int delta) {
        if (delta <= 0) {
            throw new IllegalArgumentException("증가 수량은 1 이상이어야 합니다.");
        }
        this.quantity += delta;
    }

    public long getLineTotal() {
        return unitPriceSnapshot * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartItem that)) {
            return false;
        }
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
