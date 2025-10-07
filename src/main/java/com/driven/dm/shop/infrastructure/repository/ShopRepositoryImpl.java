package com.driven.dm.shop.infrastructure.repository;

import com.driven.dm.shop.domain.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopRepository shopRepository;

}
