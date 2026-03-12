package com.driven.dm.shop.application.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.infrastructure.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest;
import com.driven.dm.shop.presentation.dto.response.ShopCreateResponse;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @Mock
    ShopRepository shopRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    ShopService shopService;

    @Test
    @DisplayName("가게 생성 성공 테스트")
    void createShop() {
        SecurityUser securityUser = new SecurityUser(UUID.randomUUID(), "testUser", "abcdefg123@@",
            UserRole.OWNER);
        User user = User.of(securityUser.getUsername(), securityUser.getPassword(), "testUser");
        user.changeRole(UserRole.OWNER);

        given(userRepository.findById(any(UUID.class)))
            .willReturn(Optional.of(user));

        given(shopRepository.save(any(Shop.class)))
            .willAnswer(inv -> inv.getArgument(0));

        ShopCreateRequest req = new ShopCreateRequest(
            "카페",
            "분위기 좋음",
            "발산 어나더사이드"
        );

        ShopCreateResponse res = shopService.createShop(securityUser, req);

        assertThat(res.shopName()).isEqualTo("카페");
        assertThat(res.description()).isEqualTo("분위기 좋음");
    }

    @Test
    @DisplayName("가게 생성 실패 테스트")
    void createShopFail() {
        SecurityUser securityUser = new SecurityUser(UUID.randomUUID(), "testUser", "abcdefg123@@",
            UserRole.CUSTOMER);

        ShopCreateRequest req = new ShopCreateRequest(
            "카페",
            "분위기 좋음",
            "발산 어나더사이드"
        );

        assertThrows(AppException.class, () -> shopService.createShop(securityUser, req));
    }

    @Test
    void selectShop() {
    }

    @Test
    void searchByShopName() {
    }

    @Test
    void searchByCategory() {
    }

    @Test
    void updateShop() {
    }

    @Test
    void deleteShop() {
    }

    @Test
    void adminShopList() {
    }
}
