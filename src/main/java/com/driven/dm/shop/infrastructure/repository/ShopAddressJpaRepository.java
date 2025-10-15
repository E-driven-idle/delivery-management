package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.entity.ShopAddress;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopAddressJpaRepository extends JpaRepository<ShopAddress, Long> {


    ShopAddress findByShopId(UUID shopId);
}
