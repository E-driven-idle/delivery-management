package com.driven.dm.shop.application.service;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopAddress;
import com.driven.dm.shop.domain.repository.ShopAddressRepository;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.AddressCreateRequest;
import com.driven.dm.shop.presentation.dto.response.AddressResponse;
import com.driven.dm.user.application.exception.UserErrorCode;
import com.driven.dm.user.domain.entity.User;
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
    private final ShopAddressRepository shopAddressRepository;

    @Transactional
    public AddressResponse create(UUID id, SecurityUser securityUser ,AddressCreateRequest req) {

        Shop shop = shopRepository.selectShop(id);
        if(shop == null){
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        User user = userRepository.findById(securityUser.getId()).orElseThrow(
            () -> new AppException(UserErrorCode.USER_NOT_FOUND)
        );

        if(shop.getOwner().getId().equals(user.getId())) {
            throw new AppException(ShopErrorCode.SHOP_NOT_OWNER);
        }

        if (shop.getAddress() != null) {
            throw new AppException(ShopErrorCode.ADDRESS_DUPLICATE);
        }

        // 카카오 API 호출
        var docOpt = kakaoLocalClient.searchFirst(req.getQuery());
        var doc = docOpt.orElseThrow(
            () -> new AppException(ShopErrorCode.ADDRESS_NO_SUCH)
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
        ShopAddress createAddress = shopAddressRepository.createShopAddress(shopAddress);

        return AddressResponse.builder()
            .id(createAddress.getId())
            .fullAddress(createAddress.getFullAddress())

            .latitude(createAddress.getLatitude())
            .longitude(createAddress.getLongitude())
            .source("kakao")
            .build();
    }

}
