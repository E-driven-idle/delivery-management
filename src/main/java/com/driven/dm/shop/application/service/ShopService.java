package com.driven.dm.shop.application.service;

import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.domain.repository.ShopRepository;
import com.driven.dm.shop.presentation.dto.request.ShopDto;
import com.driven.dm.shop.presentation.dto.response.ShopListResponseDto;
import com.driven.dm.shop.presentation.dto.response.ShopResponseDto;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public ShopResponseDto createShop(UserDetails userDetails, ShopDto shopDto) {

        // 유저 정보 확인
        Optional<User> findUser = userRepository.findByUsername(userDetails.getUsername());
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

        for (Shop shop : shopList) {
            ShopListResponseDto shopListResponse = ShopListResponseDto.builder()
                .shopName(shop.getShopName())
                .description(shop.getDescription())
                .avgRating(shop.getAvgRating())
                .build();
            shopListResponseDto.add(shopListResponse);
        }

        return shopListResponseDto;
    }

    public ShopResponseDto getShop(UUID id) {
        Shop shop = shopRepository.selectShop(id);

        return  ShopResponseDto.from(shop);
    }
}
