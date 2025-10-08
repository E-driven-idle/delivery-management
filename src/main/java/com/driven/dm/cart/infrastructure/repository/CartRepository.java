package com.driven.dm.cart.infrastructure.repository;

import com.driven.dm.cart.domain.entity.Cart;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByUser_IdAndShop_Id(UUID userId, UUID shopId);

    boolean existsByUser_IdAndShop_Id(UUID userId, UUID shopId);
}

