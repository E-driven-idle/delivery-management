package com.driven.dm.cart.infrastructure.repository;

import com.driven.dm.cart.domain.entity.CartItem;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByIdAndCart_User_IdAndDeletedAtIsNull(UUID id, UUID userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update CartItem ci
               set ci.deletedAt = CURRENT_TIMESTAMP,
                   ci.deletedBy = :userId
             where ci.id = :cartItemId
               and ci.cart.user.id = :userId
               and ci.cart.shop.id = :shopId
               and ci.deletedAt IS NULL
        """)
    int softDeleteOne(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId,
        @Param("cartItemId") UUID cartItemId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update CartItem ci
               set ci.deletedAt = CURRENT_TIMESTAMP,
                   ci.deletedBy = :userId
             where ci.cart.id = :cartId
               and ci.deletedAt IS NULL
        """)
    int softDeleteAllByCartId(@Param("userId") UUID userId,
        @Param("cartId") UUID cartId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update CartItem ci
               set ci.deletedAt = CURRENT_TIMESTAMP,
                   ci.deletedBy = :userId
             where ci.cart.user.id = :userId
               and ci.deletedAt IS NULL
        """)
    int softDeleteAllByUser(@Param("userId") UUID userId);

    @Query("""
            select count(ci)
            from CartItem ci
            where ci.cart.user.id = :userId
              and ci.cart.shop.id = :shopId
              and ci.deletedAt IS NULL
        """)
    long countAliveItemsByUserAndShop(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId);
}
