package com.driven.dm.shop.domain.entity;

import com.driven.dm.global.entity.BaseEntity;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateRequest;
import com.driven.dm.user.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import jakarta.persistence.OneToOne;
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
@Table(name = "p_shop")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shop_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "description")
    private String description;

    @Column(name = "avg_rating")
    private Double avgRating;

    @Enumerated(EnumType.STRING)
    @Column(name = "shop_status")
    private ShopStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "shop_category")
    private ShopCategory shopCategory;

    @OneToOne(mappedBy = "shop", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonManagedReference
    private ShopAddress address;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Menu> menu = new ArrayList<>();

    public static Shop of(ShopCreateRequest shopCreateRequest){

        return of(null, shopCreateRequest);
    }

    public static Shop of(User user, ShopCreateRequest shopCreateRequest){
        Shop shop = new Shop();
        shop.owner = user;
        shop.shopName = shopCreateRequest.getShopName();
        shop.description = shopCreateRequest.getDescription();
        shop.status = ShopStatus.CLOSED;
        shop.avgRating = 0.0;
        return shop;
    }

    public void update(String shopName, String description, String status){
        this.shopName = shopName;
        this.description = description;

        if(status.equals("OPEN")){
            this.status = ShopStatus.OPEN;
        }else {
            this.status = ShopStatus.CLOSED;
        }
    }

    public Shop deleteShop(UUID id){
        delete(id);

        this.status = ShopStatus.DELETED;

        return this;
    }

    public void changeName(String shopName) {
        this.shopName = shopName;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

}
