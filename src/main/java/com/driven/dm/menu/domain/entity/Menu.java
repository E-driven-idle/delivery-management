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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@Table(name = "p_menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    @Column(name = "menu_description")
    private String menuDescription;

    @Column(name = "menu_keyword")
    private String menuKeyword;

    @Column(name = "ai_support")
    private boolean aiSupport;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MenuStatus status;

    public static Menu of(Shop shop, String menuName, String menuDescription, Long menuPrice, String menuKeyword, boolean isAi) {
        Menu menu = new Menu();
        menu.shop = shop;
        menu.menuName = menuName;
        menu.menuDescription = menuDescription;
        menu.menuPrice = menuPrice;
        menu.menuKeyword = menuKeyword;
        menu.aiSupport = isAi;
        menu.status = MenuStatus.ACTIVE;
        return menu;
    }

    public void changeMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void changeMenuPrice(Long menuPrice) {
        this.menuPrice = menuPrice;
    }

    public void changeMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public void changeMenuKeyword(String menuKeyword) {
        this.menuKeyword = menuKeyword;
    }

    public void changeStatus(String status) {

        if (status.equals("open")) {
            this.status = MenuStatus.ACTIVE;
        } else {
            this.status = MenuStatus.HIDDEN;
        }
    }

    public void deleteMenu() {
        delete(this.getShop().getOwner().getId());
        this.status = MenuStatus.DELETED;
    }
}
