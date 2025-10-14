package com.driven.dm.shop.presentation.controller;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.shop.application.service.ShopAddressService;
import com.driven.dm.shop.application.service.ShopService;
import com.driven.dm.shop.presentation.dto.request.ShopAddressCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopAddressUpdateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateRequest;
import com.driven.dm.shop.presentation.dto.response.ShopAddressResponse;
import com.driven.dm.shop.presentation.dto.response.ShopCreateResponse;
import com.driven.dm.shop.presentation.dto.response.ShopListResponseDto;
import com.driven.dm.shop.presentation.dto.response.ShopResponseDto;
import com.driven.dm.shop.presentation.dto.response.ShopUpdateResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ShopAddressService shopAddressService;

    @PostMapping
    public ResponseEntity<ShopCreateResponse> createShop(
        @AuthenticationPrincipal SecurityUser securityUser,
        @RequestBody ShopCreateRequest shopCreateRequest){

        ShopCreateResponse shopCreateResponse = shopService.createShop(securityUser, shopCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(shopCreateResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ShopListResponseDto>> searchByShopName (@RequestParam("shopName") String shopName) {

        List<ShopListResponseDto> shopListResponseDto = shopService.searchByShopName(shopName);
        return ResponseEntity.ok().body(shopListResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ShopListResponseDto>> shopList(){
        List<ShopListResponseDto> shopListResponseDto = shopService.shopList();

        return ResponseEntity.ok().body(shopListResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopResponseDto> selectShop(@PathVariable UUID id) {
        ShopResponseDto shopResponseDto = shopService.selectShop(id);

        return ResponseEntity.ok().body(shopResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopUpdateResponse> updateShop(
        @PathVariable UUID id,
        @AuthenticationPrincipal SecurityUser securityUser,
        @Valid @RequestBody ShopUpdateRequest shopUpdateRequest
    ) {
        ShopUpdateResponse shopUpdateResponse = shopService.updateShop(id, securityUser,
            shopUpdateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(shopUpdateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(
        @PathVariable UUID id,
        @AuthenticationPrincipal SecurityUser securityUser
    ){
        shopService.deleteShop(id, securityUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<ShopAddressResponse> createAddress(
        @PathVariable UUID id,
        @AuthenticationPrincipal SecurityUser securityUser,
        @Valid @RequestBody ShopAddressCreateRequest shopAddressCreateRequest
    ){
        ShopAddressResponse shopAddressResponse = shopAddressService.createAddress(id, securityUser, shopAddressCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(shopAddressResponse);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<ShopAddressResponse> updateAddress(
        @PathVariable UUID id,
        @AuthenticationPrincipal SecurityUser securityUser,
        @Valid @RequestBody ShopAddressUpdateRequest shopAddressUpdateRequest
    ) {

        ShopAddressResponse shopAddressResponse = shopAddressService.updateAddress(id, securityUser, shopAddressUpdateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(shopAddressResponse);
    }

}
