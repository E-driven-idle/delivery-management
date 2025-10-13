package com.driven.dm.menu.domain.repository;

import com.driven.dm.menu.domain.entity.Menu;
import java.util.Optional;

public interface MenuRepository {

    Optional<Menu> createMenu(Menu menu);
}
