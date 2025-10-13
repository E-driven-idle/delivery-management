package com.driven.dm.cart.infrastructure.repository;

import com.driven.dm.cart.domain.entity.Cart;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {


    Optional<Cart> findByUser_IdAndShop_IdAndDeletedAtIsNull(UUID userId, UUID shopId);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Cart c
               set c.deletedAt = CURRENT_TIMESTAMP,
                   c.deletedBy = :userId
             where c.user.id = :userId
               and c.shop.id = :shopId
               and c.deletedAt IS NULL
        """)
    int softDeleteByUserAndShop(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update Cart c
               set c.deletedAt = CURRENT_TIMESTAMP,
                   c.deletedBy = :userId
             where c.user.id = :userId
               and c.deletedAt IS NULL
        """)
    int softDeleteAllByUser(@Param("userId") UUID userId);
}
