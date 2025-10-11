package com.driven.dm.cart.domain.entity;

import com.driven.dm.global.entity.BaseEntity;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = {"user", "shop", "items"})
@Table(name = "p_cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cart_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice = 0L;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CartStatus status = CartStatus.ACTIVE;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CartItem> items = new ArrayList<>();

    private Cart(User user, Shop shop) {
        this.user = user;
        this.shop = shop;
    }

    public static Cart of(User user, Shop shop) {
        return new Cart(user, shop);
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public CartItem addOrIncrease(MenuSnapshot snapshot, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("메뉴는 최소 1개 이상 담아야 합니다.");
        }

        CartItem found = items.stream()
            .filter(i -> i.getMenu().getId().equals(snapshot.menuId()))
            .findFirst()
            .orElse(null);

        if (found != null) {
            found.increase(quantity);
        } else {
            CartItem created = CartItem.of(
                this,
                snapshot.menu(),
                snapshot.menuName(),
                snapshot.unitPrice()
            );
            created.increase(quantity);
            items.add(created);
        }

        recomputeTotal();
        return found != null ? found : items.get(items.size() - 1);
    }

    public void recomputeTotal() {
        this.totalPrice = items.stream()
            .mapToLong(CartItem::getLineTotal)
            .sum();
    }

    public record MenuSnapshot(
        com.driven.dm.menu.domain.entity.Menu menu,
        UUID menuId,
        String menuName,
        long unitPrice
    ) {

    }
}
