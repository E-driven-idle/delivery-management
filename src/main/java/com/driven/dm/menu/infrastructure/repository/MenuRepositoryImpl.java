package com.driven.dm.menu.infrastructure.repository;

import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.domain.repository.MenuRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuJpaRepository menuJpaRepository;

    @Override
    public Optional<Menu> createMenu(Menu menu) {

        return menuJpaRepository.save(menu.getId());
    }
}
