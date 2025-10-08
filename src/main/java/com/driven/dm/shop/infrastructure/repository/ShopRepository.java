package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.entity.Shop;
import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {
    Optional<Shop> findById(UUID id);
}