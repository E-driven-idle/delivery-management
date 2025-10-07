package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.repository.ShopRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;

    @Override
    public Shop createShop(Shop shop) {

        return shopJpaRepository.save(shop);
    }

    @Override
    public List<Shop> getShop() {

        return shopJpaRepository.findAll();
    }
}
