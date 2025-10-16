package com.driven.dm.shop.application.service;

import static com.driven.dm.global.util.NumberUtils.round1;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.ApiErrorCode;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.domain.entity.ShopCategory;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.infrastructure.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateRequest;
import com.driven.dm.shop.presentation.dto.response.ShopCreateResponse;
import com.driven.dm.shop.presentation.dto.response.ShopListResponse;
import com.driven.dm.shop.presentation.dto.response.ShopResponse;
import com.driven.dm.shop.presentation.dto.response.ShopUpdateResponse;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Transactional
    public ShopCreateResponse createShop(SecurityUser securityUser,
        ShopCreateRequest shopCreateRequest) {

        User user = getUser(securityUser);

        boolean isOwner = user.getRole().equals(UserRole.OWNER);

        if (isOwner) {
            Shop shop = Shop.of(user, shopCreateRequest);
            Shop createdShop = shopRepository.save(shop);
            return ShopCreateResponse.builder()
                .shopName(createdShop.getShopName())
                .shopDescription(createdShop.getDescription())
                .build();

        } else {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }
    }

    @Transactional(readOnly = true)
    public List<ShopListResponse> shopList() {

        List<Shop> shopList = shopRepository.findAll();
        List<ShopListResponse> shopListResponseRequest = new ArrayList<>();

        for (Shop openShop : shopList) {
            if (openShop.getStatus().equals(ShopStatus.OPEN) || openShop.getStatus()
                .equals(ShopStatus.CLOSED)) {
                ShopListResponse shopListResponse = ShopListResponse.builder()
                    .shopName(openShop.getShopName())
                    .description(openShop.getDescription())
                    .category(openShop.getCategory().toString())
                    .avgRating(round1(openShop.getAvgRating()))
                    .fullAddress(
                        openShop.getAddress() != null
                            ? openShop.getAddress().getFullAddress()
                            : ""
                    )
                    .build();
                shopListResponseRequest.add(shopListResponse);
            }
        }

        return shopListResponseRequest;
    }

    @Transactional(readOnly = true)
    public ShopResponse selectShop(UUID id) {
        Shop shop = getShop(id);

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        String fullAddress = Optional.ofNullable(shop.getAddress())
            .map(ShopAddress::getFullAddress)
            .orElse(null);

        return ShopResponse.builder()
            .shopName(shop.getShopName())
            .description(shop.getDescription())
            .category(shop.getCategory())
            .avgRating(round1(shop.getAvgRating()))
            .shopStatus(shop.getStatus())
            .fullAddress(fullAddress)
            .build();
    }

    @Transactional(readOnly = true)
    public List<ShopListResponse> searchByShopName(String shopName) {

        List<Shop> shops = shopRepository.findByShopNameContainingAndStatusNot(shopName,
            ShopStatus.DELETED);

        return shops.stream()
            .map(shop -> ShopListResponse.builder()
                .shopName(shop.getShopName())
                .description(shop.getDescription())
                .category(shop.getCategory().toString())
                .avgRating(round1(shop.getAvgRating()))
                .fullAddress(
                    shop.getAddress() != null
                        ? shop.getAddress().getFullAddress()
                        : ""
                )
                .build())
            .toList();
    }

    @Transactional(readOnly = true)
    public List<ShopListResponse> searchByCategory(ShopCategory category) {
        List<Shop> shopList = shopRepository.findByCategoryAndStatusNot(category,
            ShopStatus.DELETED);

        return shopList.stream()
            .map(shop -> ShopListResponse.builder()
                .shopName(shop.getShopName())
                .description(shop.getDescription())
                .category(shop.getCategory().toString())
                .avgRating(round1(shop.getAvgRating()))
                .fullAddress(
                    shop.getAddress() != null
                        ? shop.getAddress().getFullAddress()
                        : ""
                )
                .build())
            .toList();
    }

    @Transactional
    public ShopUpdateResponse updateShop(UUID id, SecurityUser securityUser,
        ShopUpdateRequest shopUpdateRequest) {
        Shop shop = getShop(id);
        User user = getUser(securityUser);

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        if (isOwner(user, shop)) {
            shop.update(shopUpdateRequest.getShopName(), shopUpdateRequest.getDescription(),
                shopUpdateRequest.getStatus(), shopUpdateRequest.getCategory());
            Shop updatedShop = shopRepository.save(shop);
            return ShopUpdateResponse.builder()
                .shopName(updatedShop.getShopName())
                .description(updatedShop.getDescription())
                .shopStatus(updatedShop.getStatus())
                .category(updatedShop.getCategory())
                .build();
        } else {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }
    }

    @Transactional
    public void deleteShop(UUID id, SecurityUser securityUser) {

        Shop shop = getShop(id);
        User user = getUser(securityUser);

        boolean privileges = isOwner(user, shop)
            || user.getRole().equals(UserRole.MANAGER)
            || user.getRole().equals(UserRole.MASTER);

        if (privileges) {
            Shop deleteShop = shop.deleteShop(user.getId());
            shopRepository.save(deleteShop);
        } else {
            throw new AppException(ApiErrorCode.FORBIDDEN);
        }
    }

    private User getUser(SecurityUser securityUser) {

        return userRepository.findById(securityUser.getId()).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND));
    }

    private Shop getShop(UUID shopId) {

        return shopRepository.findById(shopId).orElseThrow(
            () -> new AppException(ShopErrorCode.SHOP_NOT_FOUND)
        );
    }

    private boolean isOwner(User user, Shop shop) {

        if (!user.getRole().equals(UserRole.OWNER)
            || !user.getId().equals(shop.getOwner().getId())) {
            return false;
        } else {
            return true;
        }
    }

}