package com.driven.dm.menu.infrastructure.repository;

import com.driven.dm.menu.domain.entity.Menu;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, UUID> {

    // 나중에 Menu에 shop 필드가 생기면 아래 같은 메서드를 추가하면 됨
    Optional<Menu> findByIdAndShop_Id(UUID menuId, UUID shopId);

    List<Menu> findAllByIdInAndShopIdAndDeletedAtIsNull(List<UUID> menuIds, UUID shopId);
}
