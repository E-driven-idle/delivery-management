package com.driven.dm.menu.domain.repository;

import com.driven.dm.menu.domain.entity.Menu;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository {

    Optional<Menu> createMenu(Menu menu);

    List<Menu> selectAll();

    Menu updateMenu(Menu menu);

    Optional<Menu> selectMenu(UUID id);
}
