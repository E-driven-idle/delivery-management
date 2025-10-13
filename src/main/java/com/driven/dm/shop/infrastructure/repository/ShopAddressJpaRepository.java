package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.entity.ShopAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopAddressJpaRepository extends JpaRepository<ShopAddress, Long> {

}
