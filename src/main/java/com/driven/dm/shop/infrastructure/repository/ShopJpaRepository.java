package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.entity.Shop;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<Shop, UUID> {

}