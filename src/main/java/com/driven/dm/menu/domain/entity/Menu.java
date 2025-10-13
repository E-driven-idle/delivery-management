package com.driven.dm.menu.domain.entity;

import com.driven.dm.global.entity.BaseEntity;
import com.driven.dm.global.exception.ApiErrorCode;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.domain.entity.Shop;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "p_menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "menu_id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "menu_price")
    private Long menuPrice;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MenuStatus status;

    public static Menu of(Shop shop, String menuName, Long menuPrice) {
        Menu menu = new Menu();
        menu.shop = shop;
        menu.menuName = menuName;
        menu.menuPrice = menuPrice;
        menu.status = MenuStatus.ACTIVE;
        return menu;
    }

    public void changeMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void changeMenuPrice(Long menuPrice) {
        this.menuPrice = menuPrice;
    }

    public void changeStatus(String status){

        if (status.equals("active")){
            this.status = MenuStatus.ACTIVE;
        } else if (status.equals("hidden")){
            this.status = MenuStatus.HIDDEN;
        } else if (status.equals("deleted")) {
            this.status = MenuStatus.DELETED;
        } else {
            throw new AppException(ApiErrorCode.INVALID_REQUEST);
        }
    }

}
