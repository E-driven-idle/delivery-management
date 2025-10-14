package com.driven.dm.shop.application.service;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.ApiErrorCode;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateRequest;
import com.driven.dm.shop.presentation.dto.response.ShopCreateResponse;
import com.driven.dm.shop.presentation.dto.response.ShopListResponseDto;
import com.driven.dm.shop.presentation.dto.response.ShopResponseDto;
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
    public ShopCreateResponse createShop(SecurityUser securityUser, ShopCreateRequest shopCreateRequest) {

        User user = getUser(securityUser);

        boolean isOwner = user.getRole().equals(UserRole.OWNER);

        if (isOwner) {
            Shop shop = Shop.of(user, shopCreateRequest);
            Shop createdShop = shopRepository.createShop(shop);
            return ShopCreateResponse.builder()
                .shopName(createdShop.getShopName())
                .shopDescription(createdShop.getDescription())
                .build();

        }else {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }
    }

    @Transactional(readOnly = true)
    public List<ShopListResponseDto> shopList() {

        List<Shop> shopList = shopRepository.getShopList();
        List<ShopListResponseDto> shopListResponseDto = new ArrayList<>();

        for (Shop openShop : shopList) {
            if(openShop.getStatus().equals(ShopStatus.OPEN) || openShop.getStatus().equals(ShopStatus.CLOSED)) {
                ShopListResponseDto shopListResponse = ShopListResponseDto.builder()
                    .shopName(openShop.getShopName())
                    .description(openShop.getDescription())
                    .category(openShop.getCategory().toString())
                    .avgRating(openShop.getAvgRating())
                    .fullAddress(
                        openShop.getAddress() != null
                            ? openShop.getAddress().getFullAddress()
                            : null
                    )
                    .build();
                shopListResponseDto.add(shopListResponse);
            }
        }

        return shopListResponseDto;
    }

    @Transactional(readOnly = true)
    public ShopResponseDto selectShop(UUID id) {
        Shop shop = getShop(id);

        if(shop == null){
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        String fullAddress = Optional.ofNullable(shop.getAddress())
            .map(ShopAddress::getFullAddress)
            .orElse(null);

        return  ShopResponseDto.builder()
            .shopName(shop.getShopName())
            .description(shop.getDescription())
            .category(shop.getCategory())
            .avgRating(shop.getAvgRating())
            .shopStatus(shop.getStatus())
            .fullAddress(fullAddress)
            .build();
    }

    @Transactional(readOnly = true)
    public List<ShopListResponseDto> searchByShopName(String shopName) {

        List<Shop> shops = shopRepository.findByShopNameContainingAndStatusNot(shopName, ShopStatus.DELETED);

        return shops.stream()
            .map(shop -> ShopListResponseDto.builder()
                .shopName(shop.getShopName())
                .description(shop.getDescription())
                .category(shop.getCategory().toString())
                .avgRating(shop.getAvgRating())
                .fullAddress(
                    shop.getAddress() != null
                        ? shop.getAddress().getFullAddress()
                        : null
                )
                .build())
            .toList();
    }

    @Transactional
    public ShopUpdateResponse updateShop(UUID id, SecurityUser securityUser, ShopUpdateRequest shopUpdateRequest) {
        Shop shop = getShop(id);
        User user = getUser(securityUser);

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        if (isOwner(user, shop)) {
            shop.update(shopUpdateRequest.getShopName(), shopUpdateRequest.getDescription(), shopUpdateRequest.getStatus(), shopUpdateRequest.getCategory());
            Shop updatedShop = shopRepository.updateShop(shop);
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
            shopRepository.updateShop(deleteShop);
        } else {
            throw new AppException(ApiErrorCode.FORBIDDEN);
        }
    }

    private User getUser(SecurityUser securityUser) {

        return userRepository.findById(securityUser.getId()).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND));
    }

    private Shop getShop(UUID shopId) {

        return shopRepository.selectShop(shopId).orElseThrow(
            () -> new AppException(ShopErrorCode.SHOP_NOT_FOUND)
        );
    }

    private boolean isOwner(User user, Shop shop) {

        if( !user.getRole().equals(UserRole.OWNER)
            || !user.getId().equals(shop.getOwner().getId())){
            return false;
        } else {
            return true;
        }
    }
}