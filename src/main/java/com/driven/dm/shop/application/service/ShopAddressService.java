package com.driven.dm.shop.application.service;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.infrastructure.repository.ShopAddressJpaRepository;
import com.driven.dm.shop.infrastructure.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.ShopAddressCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopAddressUpdateRequest;
import com.driven.dm.shop.presentation.dto.response.ShopAddressResponse;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShopAddressService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final KakaoLocalClient kakaoLocalClient;
    private final ShopAddressJpaRepository shopAddressRepository;

    @Transactional
    public ShopAddressResponse createAddress(UUID id, SecurityUser securityUser , ShopAddressCreateRequest req) {

        Shop shop = getShop(id);
        User user = getUser(securityUser);

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_ALREADY_DELETED);
        }

        if (shop.getAddress() != null) {
            throw new AppException(ShopErrorCode.ADDRESS_DUPLICATE);
        }

        if (!isOwner(user, shop)) {
            throw new  AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        // 카카오 API 호출
        var docOpt = kakaoLocalClient.searchFirst(req.getQuery());
        var doc = docOpt.orElseThrow(
            () -> new AppException(ShopErrorCode.ADDRESS_NOT_FOUND)
        );

        // 전체주소 & 좌표 추출
        String fullAddress;
        String xStr;
        String yStr;
        String region_1depth_name;
        String region_2depth_name;
        String region_3depth_name;
        String h_code;

        if (doc.getAddress() != null){
            fullAddress = doc.getAddress().getAddress_name();
            xStr = doc.getAddress().getX();
            yStr = doc.getAddress().getY();
            region_1depth_name = doc.getAddress().getRegion_1depth_name();
            region_2depth_name = doc.getAddress().getRegion_2depth_name();
            region_3depth_name = doc.getAddress().getRegion_3depth_name();
            h_code = doc.getAddress().getH_code();
        } else {
            throw new AppException(ShopErrorCode.ADDRESS_NO_STATE);
        }

        Double longitude = Double.parseDouble(xStr);
        Double latitude = Double.parseDouble(yStr);

        if(longitude == null || latitude == null){
            throw new AppException(ShopErrorCode.ADDRESS_NO_STATE);
        }

        ShopAddress shopAddress = ShopAddress.of(shop, fullAddress, latitude, longitude, region_1depth_name, region_2depth_name, region_3depth_name, h_code);
        ShopAddress createAddress = shopAddressRepository.save(shopAddress);

        return ShopAddressResponse.builder()
            .fullAddress(createAddress.getFullAddress())
            .region_1depth_name(createAddress.getRegion_1depth())
            .region_2depth_name(createAddress.getRegion_2depth())
            .region_3depth_name(createAddress.getRegion_3depth())
            .h_code(createAddress.getH_code())
            .latitude(createAddress.getLatitude())
            .longitude(createAddress.getLongitude())
            .source("kakao")
            .build();
    }

    @Transactional
    public ShopAddressResponse updateAddress(UUID id, SecurityUser securityUser, ShopAddressUpdateRequest shopAddressUpdateRequest) {

        Shop shop = getShop(id);
        User user = getUser(securityUser);

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_ALREADY_DELETED);
        }

        if (!isOwner(user, shop)) {
            throw new  AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        ShopAddress shopAddress = shopAddressRepository.findByShopId(shop.getId()).orElseThrow(
            () -> new AppException(ShopErrorCode.ADDRESS_NOT_FOUND)
        );

        var doc = kakaoLocalClient.searchFirst(shopAddressUpdateRequest.getQuery()).orElseThrow(
            () -> new AppException(ShopErrorCode.ADDRESS_NOT_FOUND)
        );

        var addr = doc.getAddress();

        if (addr == null) {
            throw new AppException(ShopErrorCode.ADDRESS_NO_STATE);
        }

        Double longitude = Double.parseDouble(addr.getX());
        Double latitude = Double.parseDouble(addr.getY());

        shopAddress.updateAddress(
            addr.getAddress_name(),
            latitude,
            longitude,
            addr.getRegion_1depth_name(),
            addr.getRegion_2depth_name(),
            addr.getRegion_3depth_name(),
            addr.getH_code()
        );

        return ShopAddressResponse.builder()
            .fullAddress(shopAddress.getFullAddress())
            .region_1depth_name(shopAddress.getRegion_1depth())
            .region_2depth_name(shopAddress.getRegion_2depth())
            .region_3depth_name(shopAddress.getRegion_3depth())
            .h_code(shopAddress.getH_code())
            .latitude(shopAddress.getLatitude())
            .longitude(shopAddress.getLongitude())
            .source("kakao")
            .build();
    }

    @Transactional
    public void deleteAddress(UUID id, SecurityUser securityUser) {

        Shop shop = getShop(id);
        User user = getUser(securityUser);

        if (shop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_ALREADY_DELETED);
        }

        boolean privileged = isOwner(user, shop)
            || user.getRole().equals(UserRole.MANAGER)
            || user.getRole().equals(UserRole.MASTER);

        if (!privileged) {
            throw new  AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        ShopAddress shopAddress = getShopAddress(shop);
        shopAddress.deleteAddress();
        shopAddressRepository.save(shopAddress);
    }

    private Shop getShop(UUID id) {

        return shopRepository.findById(id).orElseThrow(
            () -> new AppException(ShopErrorCode.SHOP_NOT_FOUND)
        );
    }

    private User getUser(SecurityUser securityUser) {

        return userRepository.findById(securityUser.getId()).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND)
        );
    }

    private ShopAddress getShopAddress(Shop shop) {

        return shopAddressRepository.findByShopId(shop.getId()).orElseThrow(
            () -> new AppException(ShopErrorCode.ADDRESS_NOT_FOUND)
        );
    }

    private boolean isOwner(User user, Shop shop) {

        if(user.getId().equals(shop.getOwner().getId())) {
            return true;
        }
        return false;
    }
}
