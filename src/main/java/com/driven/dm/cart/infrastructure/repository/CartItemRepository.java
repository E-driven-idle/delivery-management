package com.driven.dm.cart.infrastructure.repository;

import com.driven.dm.cart.domain.entity.CartItem;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    @Query("""
        select ci
        from CartItem ci
        join fetch ci.menu m
        where ci.cart.id = :cartId
        """)
    List<CartItem> findAllByCart_IdWithMenu(UUID cartId);

    Optional<CartItem> findByCart_IdAndMenu_Id(UUID cartId, UUID menuId);

    long deleteByCart_Id(UUID cartId);
}
