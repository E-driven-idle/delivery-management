package com.driven.dm.menu.application.service;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.application.exception.MenuErrorCode;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.domain.entity.MenuStatus;
import com.driven.dm.menu.domain.repository.MenuRepository;
import com.driven.dm.menu.presentation.dto.request.MenuCreateRequest;
import com.driven.dm.menu.presentation.dto.request.MenuUpdateRequest;
import com.driven.dm.menu.presentation.dto.response.MenuCreateResponse;
import com.driven.dm.menu.presentation.dto.response.MenuListResponse;
import com.driven.dm.menu.presentation.dto.response.MenuUpdateResponse;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
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

    public MenuCreateResponse createMenu(UUID id, MenuCreateRequest menuCreateRequest) {
        Shop shop = getShop(id);
        Optional<User> user = userRepository.findById(shop.getOwner().getId());

        if (isOwner(user.get(), shop)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        Menu menu = Menu.of(shop, menuCreateRequest.getMenuname(),
            menuCreateRequest.getMenuprice());
        Menu saveMenu = menuRepository.createMenu(menu).orElseThrow(
            () -> new AppException(MenuErrorCode.MENU_SAVE_FAIL)
        );

        return MenuCreateResponse.builder()
            .menuId(saveMenu.getId())
            .menuName(saveMenu.getMenuName())
            .menuPrice(saveMenu.getMenuPrice())
            .build();
    }

    public List<MenuListResponse> menuList(SecurityUser securityUser) {

        List<Menu> menus = menuRepository.selectAll();
        List<MenuListResponse> menuListResponses = new ArrayList<>();

        if (securityUser.getRole().equals(UserRole.MASTER)
            || securityUser.getRole().equals(UserRole.MANAGER)) {
            for (Menu menu : menus) {
                MenuListResponse listResponse = MenuListResponse.from(menu);
                menuListResponses.add(listResponse);
            }
            return menuListResponses;
        } else {
            for (Menu menu : menus) {
                if (menu.getStatus().equals(MenuStatus.ACTIVE)) {
                    MenuListResponse listResponse = MenuListResponse.from(menu);
                    menuListResponses.add(listResponse);
                }
            }
            return menuListResponses;
        }
    }

    public MenuUpdateResponse updateMenu(UUID id, SecurityUser securityUser,
        MenuUpdateRequest menuUpdateRequest) {
        User user = getUser(securityUser);
        Menu menu = menuRepository.selectMenu(id).orElseThrow(
            () -> new AppException(MenuErrorCode.MENU_NOT_FOUND)
        );

        if (!isOwner(user, menu.getShop())) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        menu.changeMenuName(menuUpdateRequest.getMenuName());
        menu.changeMenuPrice(menuUpdateRequest.getMenuPrice());
        menu.changeStatus(menuUpdateRequest.getMenuStatus());

        Menu updatedMenu = menuRepository.updateMenu(menu);

        return MenuUpdateResponse.builder()
            .menuName(updatedMenu.getMenuName())
            .menuPrice(updatedMenu.getMenuPrice())
            .build();
    }

    public void deleteMenu(UUID id, SecurityUser securityUser) {
        Menu menu = getMenu(id);
        User user = getUser(securityUser);

        if (!isOwner(user, menu.getShop())) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        menu.deleteMenu();
        menuRepository.deleteMenu(menu);
    }

    private Menu getMenu(UUID id) {

        return menuRepository.selectMenu(id).orElseThrow(
            () -> new AppException(MenuErrorCode.MENU_NOT_FOUND)
        );
    }

    private Shop getShop(UUID id) {
        Shop shop = shopRepository.selectShop(id);
        if (shop == null) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }
        return shop;
    }

    private User getUser(SecurityUser securityUser) {

        return userRepository.findById(securityUser.getId()).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND)
        );
    }

    private boolean isOwner(User user, Shop shop) {

        if (user.getId().equals(shop.getOwner().getId())) {
            return true;
        } else {
            return false;
        }
    }
}
