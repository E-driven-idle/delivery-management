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
        select ci.id, ci.deletedAt, c.deletedAt, s.deletedAt
        from CartItem ci
        join ci.cart c
        join c.shop s
        where c.user.id = :userId and c.shop.id = :shopId
        order by ci.createdAt desc
        """)
    java.util.List<Object[]> probeRaw(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId);

    @Query("""
        select count(ci)
        from CartItem ci
        join ci.cart c
        join c.shop s
        where c.user.id = :userId
          and c.shop.id = :shopId
          and ci.deletedAt IS NULL
          and c.deletedAt  IS NULL
          and s.deletedAt  IS NULL
        """)
    long probeFilteredCount(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId);


    @Query(
        value = """
            select new com.driven.dm.cart.presentation.dto.response.ShopCartItemDto(
                ci.id, ci.menu.id, ci.menuNameSnapshot, ci.quantity,
                ci.unitPriceSnapshot, ci.unitPriceSnapshot * ci.quantity
            )
            from CartItem ci
            join ci.cart c
            join c.shop s
            where c.user.id = :userId
              and c.shop.id = :shopId
              and ci.deletedAt IS NULL
              and c.deletedAt  IS NULL
              and s.deletedAt  IS NULL
            order by ci.createdAt desc
            """,
        countQuery = """
            select count(ci.id)
            from CartItem ci
            join ci.cart c
            join c.shop s
            where c.user.id = :userId
              and c.shop.id = :shopId
              and ci.deletedAt IS NULL
              and c.deletedAt  IS NULL
              and s.deletedAt  IS NULL
            """
    )
    Page<ShopCartItemDto> findShopCartItems(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId,
        Pageable pageable);


    @Query("""
        select coalesce(sum(ci.unitPriceSnapshot * ci.quantity), 0)
        from CartItem ci
        join ci.cart c
        join c.shop s
        where c.user.id = :userId
          and c.shop.id = :shopId
          and ci.deletedAt IS NULL
          and c.deletedAt  IS NULL
          and s.deletedAt  IS NULL
        """)
    long sumShopCartTotal(@Param("userId") UUID userId,
        @Param("shopId") UUID shopId);


    @Query(
        value = """
            select new com.driven.dm.cart.presentation.dto.response.UserCartSummaryDto(
                c.shop.id, s.shopName,
                coalesce(sum(ci.unitPriceSnapshot * ci.quantity), 0)
            )
            from CartItem ci
            join ci.cart c
            join c.shop s
            where c.user.id = :userId
              and ci.deletedAt IS NULL
              and c.deletedAt  IS NULL
              and s.deletedAt  IS NULL
            group by c.shop.id, s.shopName
            order by max(ci.createdAt) desc
            """,
        countQuery = """
            select count(distinct c.shop.id)
            from CartItem ci
            join ci.cart c
            join c.shop s
            where c.user.id = :userId
              and ci.deletedAt IS NULL
              and c.deletedAt  IS NULL
              and s.deletedAt  IS NULL
            """
    )
    Page<UserCartSummaryDto> findUserCartSummaries(@Param("userId") UUID userId,
        Pageable pageable);


    @Query("""
        select coalesce(sum(ci.unitPriceSnapshot * ci.quantity), 0)
        from CartItem ci
        join ci.cart c
        join c.shop s
        where c.user.id = :userId
          and ci.deletedAt IS NULL
          and c.deletedAt  IS NULL
          and s.deletedAt  IS NULL
        """)
    long sumUserGrandTotal(@Param("userId") UUID userId);
}
