package com.driven.dm.shop.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_shop_address")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shop_address_id", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    @JsonBackReference
    private Shop shop;

    @Column(name = "full_address", nullable = false, length = 300)
    private String fullAddress;

    @Column(name = "x")
    private Double longitude;

    @Column(name = "y")
    private Double latitude;

    @Column(name = "province_name")
    private String region_1depth;

    @Column(name = "city_name")
    private String region_2depth;

    @Column(name = "district_name")
    private String region_3depth;

    @Column(name = "administrative_code")
    private String h_code;

    public static ShopAddress of(
            Shop shop,
            String fullAddress,
            Double longitude,
            Double latitude,
            String region_1depth_name,
            String region_2depth_name,
            String region_3depth_name,
            String h_code) {

        ShopAddress shopAddress = new ShopAddress();
        shopAddress.shop = shop;
        shopAddress.fullAddress = fullAddress;
        shopAddress.region_1depth = region_1depth_name;
        shopAddress.region_2depth = region_2depth_name;
        shopAddress.region_3depth = region_3depth_name;
        shopAddress.h_code = h_code;
        shopAddress.longitude = longitude;
        shopAddress.latitude = latitude;
        return shopAddress;
    }

    public void updateAddress(String fullAddress, Double longitude, Double latitude,
                                String region_1depth, String region_2depth, String region_3depth, String h_code) {
        this.fullAddress = fullAddress;
        this.longitude = longitude;
        this.latitude = latitude;
        this.region_1depth = region_1depth;
        this.region_2depth = region_2depth;
        this.region_3depth = region_3depth;
        this.h_code = h_code;
    }

}