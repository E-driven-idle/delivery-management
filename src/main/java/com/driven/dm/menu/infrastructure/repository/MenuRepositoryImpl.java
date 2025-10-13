package com.driven.dm.menu.infrastructure.repository;

import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.domain.repository.MenuRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuJpaRepository menuJpaRepository;

    @Override
    public Optional<Menu> createMenu(Menu menu) {

        Menu savedMenu = menuJpaRepository.save(menu);
        return Optional.of(savedMenu);
    }

    @Override
    public List<Menu> selectAll() {

        return menuJpaRepository.findAll();
    }

    @Override
    public Menu updateMenu(Menu menu) {

        return menuJpaRepository.save(menu);
    }

    @Override
    public Optional<Menu> selectMenu(UUID id) {

        return menuJpaRepository.findById(id);
    }
}
