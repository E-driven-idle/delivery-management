package com.driven.dm.shop.application.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.infrastructure.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest_Delete;
import com.driven.dm.shop.presentation.dto.response.ShopCreateResponse;
import com.driven.dm.shop.presentation.dto.response.ShopListResponse;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

        assertThat(res.getShopName()).isEqualTo("카페");
        assertThat(res.getShopDescription()).isEqualTo("분위기 좋음");
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
    @DisplayName("가게 리스트 조회")
    void shopList() {
        SecurityUser securityUser = new SecurityUser(UUID.randomUUID(), "testUser", "abcdefg123@@",
            UserRole.CUSTOMER);
        User user = User.of(securityUser.getUsername(), securityUser.getPassword(), "testUser");
        user.changeRole(UserRole.OWNER);

        Shop shop1 = Shop.of(user, ShopCreateRequest_Delete.builder()
            .shopName("카페")
            .description("분위기 좋음")
            .build());

        Shop shop2 = Shop.of(user, ShopCreateRequest_Delete.builder()
            .shopName("식당")
            .description("맛있음")
            .build());

        List<Shop> shopList = List.of(shop1, shop2);
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "createdAt");
        Page<Shop> shopPage = new PageImpl<>(shopList, pageable, shopList.size());

        given(shopRepository.findByStatusNot(eq(ShopStatus.DELETED), any(Pageable.class)))
            .willReturn(shopPage);

        // when
        Page<ShopListResponse> result = shopService.shopList(0, 10, Sort.Direction.ASC);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getShopName()).isEqualTo("카페");
        assertThat(result.getContent().get(1).getDescription()).isEqualTo("맛있음");
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
