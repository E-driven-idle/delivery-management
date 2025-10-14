package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.response.ShopListResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;

    @Override
    public Shop createShop(Shop shop) {

        return shopJpaRepository.save(shop);
    }

    @Override
    public List<Shop> getShopList() {

        return shopJpaRepository.findAll();
    }

    @Override
    public Optional<Shop> selectShop(UUID id) {

        return shopJpaRepository.findById(id);
    }

    @Override
    public Shop updateShop(Shop shop) {

        return shopJpaRepository.save(shop);
    }

    @Override
    public Optional<Shop> findByIdWithMenus(UUID id) {

        return shopJpaRepository.findByIdWithMenus(id);
    }

    @Override
    public List<Shop> findByShopNameContainingAndStatusNot(String shopName, ShopStatus status) {

        return shopJpaRepository.findByShopNameContainingAndStatusNot(shopName, status);
    }
}
