package com.driven.dm.cart.infrastructure.repository;

import com.driven.dm.cart.presentation.dto.response.ShopCartItemDto;
import com.driven.dm.cart.presentation.dto.response.UserCartSummaryDto;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface CartReadRepository extends
    Repository<com.driven.dm.cart.domain.entity.CartItem, UUID> {


    @Query("""
        select new com.driven.dm.cart.presentation.dto.response.ShopCartItemDto(
            ci.id, ci.menu.id, ci.menuNameSnapshot, ci.quantity,
            ci.unitPriceSnapshot, ci.unitPriceSnapshot * ci.quantity
        )
        from CartItem ci
        join ci.cart c
        where c.user.id = :userId and c.shop.id = :shopId
        order by ci.createdAt desc
        """)
    Page<ShopCartItemDto> findShopCartItems(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId,
        Pageable pageable);


    @Query("""
        select coalesce(sum(ci.unitPriceSnapshot * ci.quantity), 0)
        from CartItem ci
        join ci.cart c
        where c.user.id = :userId and c.shop.id = :shopId
        """)
    long sumShopCartTotal(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId);


    @Query("""
        select new com.driven.dm.cart.presentation.dto.response.UserCartSummaryDto(
            c.shop.id, s.shopName,
            coalesce(sum(ci.unitPriceSnapshot * ci.quantity), 0)
        )
        from Cart c
        join c.shop s
        left join c.items ci
        where c.user.id = :userId
        group by c.shop.id, s.shopName
        order by max(c.updatedAt) desc
        """)
    Page<UserCartSummaryDto> findUserCartSummaries(@Param("userId") UUID userId,
        Pageable pageable);

    @Query("""
        select coalesce(sum(ci.unitPriceSnapshot * ci.quantity), 0)
        from Cart c
        left join c.items ci
        where c.user.id = :userId
        """)
    long sumUserGrandTotal(@Param("userId") UUID userId);
}
