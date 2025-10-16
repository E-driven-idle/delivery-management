package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopCategory;
import com.driven.dm.shop.domain.entity.ShopStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopRepository extends JpaRepository<Shop, UUID> {

    @Query("SELECT s FROM Shop s JOIN FETCH s.menu WHERE s.id = :shopId")
    Optional<Shop> findByIdWithMenus(@Param("shopId") UUID id);

    Optional<Shop> findByMenu_Id(UUID menu_id);

    @Query("""
          SELECT s FROM Shop s
          WHERE LOWER(s.shopName) LIKE LOWER(CONCAT('%', :shopName, '%'))
          AND s.status <> :status
          """)
    Page<Shop> findByShopNameContainingAndStatusNot(@Param("shopName") String shopName, @Param("status") ShopStatus status, Pageable pageable);

    @Query("""
          SELECT s
          FROM Shop s
          WHERE s.category = :category
          AND s.status <> :status
          """)
    Page<Shop> findByCategoryAndStatusNot(ShopCategory category,  ShopStatus status, Pageable pageable);

    Page<Shop> findByStatusNot(ShopStatus status, Pageable pageable);

}
