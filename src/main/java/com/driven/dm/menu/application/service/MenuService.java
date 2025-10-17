package com.driven.dm.menu.application.service;

import com.driven.dm.ai.application.service.AiService;
import com.driven.dm.ai.presentation.dto.response.AiCallResponseDto;
import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.application.exception.MenuErrorCode;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.domain.entity.MenuStatus;
import com.driven.dm.menu.infrastructure.repository.MenuRepository;
import com.driven.dm.menu.presentation.dto.request.MenuCreateRequest;
import com.driven.dm.menu.presentation.dto.request.MenuUpdateRequest;
import com.driven.dm.menu.presentation.dto.response.MenuCreateResponse;
import com.driven.dm.menu.presentation.dto.response.MenuListResponse;
import com.driven.dm.menu.presentation.dto.response.MenuShopResponse;
import com.driven.dm.menu.presentation.dto.response.MenuShopResponse.MenuResponse;
import com.driven.dm.menu.presentation.dto.response.MenuUpdateResponse;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.infrastructure.repository.ShopRepository;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final AiService aiService;

    @Transactional
    public MenuCreateResponse createMenu(UUID shop_id, SecurityUser securityUser, MenuCreateRequest menuCreateRequest) {

        String menuDescription;
        boolean isAi;

        Shop shop = getShop(shop_id);
        User user = getUser(securityUser);

        boolean isPrivileged =
            isOwner(user, shop)
            || user.getRole() == UserRole.MANAGER
            || user.getRole() == UserRole.MASTER;

        if (!isPrivileged) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        if (menuCreateRequest.isGenerateDescription()
            && !menuCreateRequest.getKeyword().isEmpty()
            && !menuCreateRequest.getFeatures().isEmpty()) {
            AiCallResponseDto aiCallResponseDto = aiService.generateMenuDescription(
                user.getId(),
                menuCreateRequest.getMenuName(),
                menuCreateRequest.getKeyword(),
                menuCreateRequest.getFeatures());
            menuDescription = aiCallResponseDto.getOutputText();
            isAi = true;
        } else {
            menuDescription = menuCreateRequest.getMenuDescription();
            isAi = false;
        }

        Menu menu =
            Menu.of(shop,
                menuCreateRequest.getMenuName(),
                menuDescription,
                menuCreateRequest.getMenuPrice(),
                menuCreateRequest.getKeyword(), isAi);

        Menu saveMenu = menuRepository.save(menu);

        return MenuCreateResponse.builder()
            .menuName(saveMenu.getMenuName())
            .menuDescription(saveMenu.getMenuDescription())
            .menuKeyword(saveMenu.getMenuKeyword())
            .menuPrice(saveMenu.getMenuPrice())
            .build();
    }

    @Transactional(readOnly = true)
    public Page<MenuListResponse> menuList(SecurityUser securityUser, int page, int size, Sort.Direction direction) {

        Pageable pageable = getPageable(page, size, direction);

        boolean isPrivileged =
            securityUser.getRole().equals(UserRole.MASTER)
                || securityUser.getRole().equals(UserRole.MANAGER);

        Set<MenuStatus> statuses = isPrivileged
            ? EnumSet.of(MenuStatus.ACTIVE, MenuStatus.HIDDEN)
            : EnumSet.of(MenuStatus.ACTIVE);

        return menuRepository.findByStatusIn(statuses, pageable)
            .map(MenuListResponse::from);
    }

    @Transactional(readOnly = true)
    public Page<MenuListResponse> searchByMenuName(String menuName, int page, int size, Sort.Direction direction) {
        Pageable pageable = getPageable(page, size, direction);

        String query = menuName.isEmpty() ? "" : menuName.trim();
        Page<Menu> menuListPage = menuRepository.findByMenuNameContainingAndStatusNot(query, MenuStatus.DELETED, pageable);

        return menuListPage.map(MenuListResponse::from);
    }

    @Transactional
    public MenuUpdateResponse updateMenu(UUID id, SecurityUser securityUser,
        MenuUpdateRequest menuUpdateRequest) {

        User user = getUser(securityUser);
        Menu menu = menuRepository.findById(id).orElseThrow(
            () -> new AppException(MenuErrorCode.MENU_NOT_FOUND)
        );

        if (menu.getStatus().equals(MenuStatus.DELETED)) {
            throw new AppException(MenuErrorCode.MENU_NOT_FOUND);
        }

        boolean isPrivileged =
            isOwner(user, menu.getShop())
                || user.getRole() == UserRole.MANAGER
                || user.getRole() == UserRole.MASTER;

        if (!isPrivileged) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        if (!menuUpdateRequest.getMenuName().isEmpty()) {
            menu.changeMenuName(menuUpdateRequest.getMenuName());
        }

        if (!menuUpdateRequest.getMenuPrice().isEmpty()) {
            menu.changeMenuPrice(Long.parseLong(menuUpdateRequest.getMenuPrice()));
        }

        if (!menuUpdateRequest.getMenuDescription().isEmpty()) {
            menu.changeMenuDescription(menuUpdateRequest.getMenuDescription());
        }

        if (!menuUpdateRequest.getMenuKeyword().isEmpty()) {
            menu.changeMenuKeyword(menuUpdateRequest.getMenuKeyword());
        }

        if (!menuUpdateRequest.getMenuStatus().isEmpty()) {
            menu.changeStatus(menuUpdateRequest.getMenuStatus());
        }

        Menu updatedMenu = menuRepository.save(menu);

        return MenuUpdateResponse.builder()
            .menuName(updatedMenu.getMenuName())
            .menuPrice(updatedMenu.getMenuPrice())
            .build();
    }

    @Transactional
    public void deleteMenu(UUID id, SecurityUser securityUser) {
        Menu menu = getMenu(id);
        User user = getUser(securityUser);

        if (menu.getStatus().equals(MenuStatus.DELETED)) {
            throw new AppException(MenuErrorCode.MENU_NOT_FOUND);
        }

        if (!isOwner(user, menu.getShop())) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        menu.deleteMenu();
        menuRepository.save(menu);
    }


    @Transactional(readOnly = true)
    public MenuShopResponse shopMenuList(UUID shop_id, SecurityUser securityUser) {

        User user = getUser(securityUser);
        Shop shop = shopRepository.findById(shop_id).orElseThrow(
            () -> new AppException(ShopErrorCode.SHOP_NOT_FOUND)
        );

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        boolean isPrivileged =
            isOwner(user, shop)
                || user.getRole() == UserRole.MANAGER
                || user.getRole() == UserRole.MASTER;

        List<MenuResponse> menuResponses = shop.getMenu().stream()
            .filter(menu -> {
                if (isPrivileged) {
                    return menu.getStatus() == MenuStatus.ACTIVE
                        || menu.getStatus() == MenuStatus.HIDDEN;
                } else {
                    return menu.getStatus() == MenuStatus.ACTIVE;
                }
            })
            .map(menu -> MenuResponse.builder()
                .menuName(menu.getMenuName())
                .menuDescription(menu.getMenuDescription())
                .menuKeyword(menu.getMenuKeyword())
                .menuPrice(menu.getMenuPrice())
                .build())
            .toList();

        return MenuShopResponse.builder()
            .shopName(shop.getShopName())
            .menus(menuResponses)
            .build();
    }

    private Pageable getPageable(int page, int size, Sort.Direction direction) {
        int safePage = Math.max(0, page);
        int safeSize = (size <= 0) ? 10 : size;
        Sort.Direction safeDir = (direction == null) ? Direction.DESC : direction;

        return  PageRequest.of(safePage, safeSize, Sort.by(safeDir, "createdAt"));
    }

    private Menu getMenu(UUID id) {

        return menuRepository.findById(id).orElseThrow(
            () -> new AppException(MenuErrorCode.MENU_NOT_FOUND)
        );
    }

    private Shop getShop(UUID id) {
        Shop shop = shopRepository.findById(id).orElseThrow(
            () -> new AppException(ShopErrorCode.SHOP_NOT_FOUND)
        );

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
