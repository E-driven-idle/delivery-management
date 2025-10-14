package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.entity.Shop;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopJpaRepository extends JpaRepository<Shop, UUID> {

    @Query("SELECT s FROM Shop s JOIN FETCH s.menu WHERE s.id = :shopId")
    Optional<Shop> findByIdWithMenus(@Param("shopId") UUID id);

}
