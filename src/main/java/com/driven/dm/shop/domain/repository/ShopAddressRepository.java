package com.driven.dm.shop.domain.repository;

import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.presentation.dto.response.ShopAddressResponse;
import java.util.Optional;
import java.util.UUID;

public interface ShopAddressRepository {

    ShopAddress createShopAddress(ShopAddress shopAddress);

    Optional<ShopAddress> selectAddress(UUID id);

    void deleteAddress(ShopAddress shopAddress);
}
