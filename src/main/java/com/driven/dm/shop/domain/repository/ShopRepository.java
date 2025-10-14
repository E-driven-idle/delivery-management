package com.driven.dm.shop.domain.repository;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopCategory;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.presentation.dto.response.ShopListResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShopRepository {

    Shop createShop(Shop shop);

    List<Shop> getShopList();

    Optional<Shop> selectShop(UUID id);

    Shop updateShop(Shop shop);

    Optional<Shop> findByIdWithMenus(UUID id);

    List<Shop> findByShopNameContainingAndStatusNot(String shopName, ShopStatus status);

    List<Shop> findByCategoryAndStatusNot(ShopCategory category, ShopStatus status);
}
