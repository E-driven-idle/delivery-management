package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.domain.repository.ShopAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShopAddressRepositoryImpl implements ShopAddressRepository {

    private final ShopAddressJpaRepository shopAddressJpaRepository;

    @Override
    public ShopAddress createShopAddress(ShopAddress shopAddress) {

        return shopAddressJpaRepository.save(shopAddress);
    }
}
