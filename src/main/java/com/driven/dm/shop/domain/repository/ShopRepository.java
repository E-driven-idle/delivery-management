package com.driven.dm.shop.domain.repository;

import com.driven.dm.shop.domain.entity.Shop;
import java.util.List;
import java.util.UUID;

public interface ShopRepository {

    Shop createShop(Shop shop);

    List<Shop> getShopList();

    Shop selectShop(UUID id);
}
