package com.driven.dm.shop.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private Shop shop;

    @Column(name = "primary_address")
    private String primaryAddress;

    @Column(name = "detail_address")
    private String detailAddress;
}