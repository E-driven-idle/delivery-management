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

    public Optional<Menu> createMenu(Menu menu) {

        Menu savedMenu = menuJpaRepository.save(menu);
        return Optional.of(savedMenu);
    }
}
