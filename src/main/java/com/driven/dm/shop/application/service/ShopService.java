package com.driven.dm.shop.application.service;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.shop.application.exception.ShopErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.entity.ShopStatus;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.ShopDto;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateDto;
import com.driven.dm.shop.presentation.dto.response.ShopListResponseDto;
import com.driven.dm.shop.presentation.dto.response.ShopResponseDto;
import com.driven.dm.user.domain.entity.User;
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

    public ShopResponseDto createShop(SecurityUser securityUser, ShopDto shopDto) {

        // 유저 정보 확인
        Optional<User> findUser = userRepository.findById(securityUser.getId());
        if (!findUser.isPresent()) {
            // TODO
            // 회원이 존재하지 않는다면 에러 처리
        }

        if(!findUser.get().getRole().toString().equals("ROLE_OWNER")){
            // TODO
            // 권한이 사장이 아닌 경우 에러 처리
        }

        if (findUser.get().getStatus().toString().equals("DELETED")) {
            // TODO
            // 아이디를 삭제한 사장은 추가할 수 없음!
        }

        Shop shop = Shop.of(findUser.get(), shopDto);
        Shop createdShop = shopRepository.createShop(shop);

        return ShopResponseDto.from(createdShop);
    }

    public List<ShopListResponseDto> getShopList() {

        List<Shop> shopList = shopRepository.getShopList();
        List<ShopListResponseDto> shopListResponseDto = new ArrayList<>();

        for (Shop openShop : shopList) {
            if(openShop.getStatus().equals(ShopStatus.OPEN) || openShop.getStatus().equals(ShopStatus.CLOSED)) {
                ShopListResponseDto shopListResponse = ShopListResponseDto.builder()
                    .shopName(openShop.getShopName())
                    .description(openShop.getDescription())
                    .avgRating(openShop.getAvgRating())
                    .address(openShop.getAddress())
                    .build();
                shopListResponseDto.add(shopListResponse);
            }
        }

        return shopListResponseDto;
    }

    @Transactional(readOnly = true)
    public ShopResponseDto getShop(UUID id) {
        Shop selectShop = shopRepository.selectShop(id);

        if(selectShop == null){
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        return  ShopResponseDto.from(selectShop);
    }

    public ShopResponseDto updateShop(UUID id, SecurityUser securityUser, ShopUpdateDto shopUpdateDto) {
        Shop selectShop = shopRepository.selectShop(id);
        Optional<User> user = userRepository.findById(securityUser.getId());
        if(!user.get().getRole().toString().equals("ROLE_OWNER")){
            // TODO
            // 사장이 아닐 시 예외 처리
        }

        if (!(user.get().getId() == selectShop.getOwner().getId())) {
            // TODO
            // 본인이 등록한 가게가 아니라면 수정 불가능한 예외 처리
        }

        if (selectShop.getId() == null) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        if (selectShop.getStatus().equals(ShopStatus.DELETED)) {
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        selectShop.update(shopUpdateDto);
        Shop shop = shopRepository.updateShop(selectShop);
        return ShopResponseDto.from(shop);
    }

    public void deleteShop(UUID id, SecurityUser securityUser) {
        Shop shop = shopRepository.selectShop(id);
        Optional<User> user = userRepository.findById(securityUser.getId());

        if(shop == null){
            throw new AppException(ShopErrorCode.SHOP_NOT_FOUND);
        }

        if(!user.get().getRole().toString().equals("ROLE_OWNER")){
            // 사장이 아님
        }

        if(!(user.get().getId() == shop.getId())) {
            // 본인 가게가 아님
        }

        Shop deleteShop = shop.deleteShop(user.get().getId());
        shopRepository.updateShop(deleteShop);
    }
}
