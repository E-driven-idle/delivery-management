package com.driven.dm.shop.domain.entity;

import com.driven.dm.global.entity.BaseEntity;
import com.driven.dm.shop.presentation.dto.request.ShopDto;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateDto;
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

    @OneToOne(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private ShopAddress address;

    public static Shop of(ShopDto shopDto){

        return of(null, shopDto);
    }

    public static Shop of(User user,ShopDto shopDto){
        Shop shop = new Shop();
        shop.owner = user;
        shop.shopName = shopDto.getShopname();
        shop.description = shopDto.getDescription();
        shop.status = ShopStatus.CLOSED;
        shop.avgRating = 0.0;
        return shop;
    }

    public Shop update(ShopUpdateDto shopUpdateDto){
        this.shopName = shopUpdateDto.getShopname();
        this.description = shopUpdateDto.getDescription();

        if(shopUpdateDto.getShopstatus().equals("OPEN")){
            this.status = ShopStatus.OPEN;
        }else {
            this.status = ShopStatus.CLOSED;
        }
        return this;
    }
}
