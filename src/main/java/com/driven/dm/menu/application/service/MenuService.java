package com.driven.dm.menu.application.service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.domain.entity.MenuStatus;
import com.driven.dm.menu.domain.repository.MenuRepository;
import com.driven.dm.menu.presentation.dto.request.MenuCreateDto;
import com.driven.dm.menu.presentation.dto.response.MenuCreateResponse;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;

    public MenuCreateResponse createMenu(UUID id, MenuCreateDto menuCreateDto) {

        Shop shop = shopRepository.selectShop(id);
        Optional<User> user = userRepository.findById(shop.getOwner().getId());

        if(user.isEmpty()){
            // 해당 가게에 저장 되어 있는 가게 주인이 없음
        }

        if(!user.get().getId().equals(shop.getOwner().getId())){
            // 가게 주인이 아니라면
        }

        if(shop == null){
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        Menu menu = Menu.of(shop, menuCreateDto.getMenuname(), menuCreateDto.getMenuprice(), MenuStatus.ACTIVE);
        Optional<Menu> saveMenu = menuRepository.createMenu(menu);

        return MenuCreateResponse.builder()
            .menuId(saveMenu.get().getId())
            .menuName(saveMenu.get().getMenuName())
            .menuPrice(saveMenu.get().getMenuPrice())
            .build();
    }

}
