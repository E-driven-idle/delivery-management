package com.driven.dm.shop.domain.repository;

import com.driven.dm.shop.domain.entity.Shop;
import java.util.List;

public interface ShopRepository {

    Shop createShop(Shop shop);

    List<Shop> getShop();
}
