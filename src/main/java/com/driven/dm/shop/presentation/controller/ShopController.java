package com.driven.dm.shop.presentation.controller;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.shop.application.service.ShopService;
import com.driven.dm.shop.presentation.dto.request.ShopDto;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateDto;
import com.driven.dm.shop.presentation.dto.response.ShopListResponseDto;
import com.driven.dm.shop.presentation.dto.response.ShopResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    public ResponseEntity<ShopResponseDto> createShop(
        @AuthenticationPrincipal SecurityUser securityUser,
        @RequestBody ShopDto shopDto){

        ShopResponseDto shopResponseDto = shopService.createShop(securityUser, shopDto);

        return ResponseEntity.ok().body(shopResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ShopListResponseDto>> getShopList(){
        List<ShopListResponseDto> shopListResponseDto = shopService.getShopList();

        return ResponseEntity.ok().body(shopListResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopResponseDto> getShop(@PathVariable UUID id) {
        ShopResponseDto shopResponseDto = shopService.getShop(id);

        return ResponseEntity.ok().body(shopResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShopResponseDto> updateShop(
        @PathVariable UUID id,
        @AuthenticationPrincipal SecurityUser securityUser,
        @RequestBody ShopUpdateDto shopUpdateDto
    ) {
        ShopResponseDto shopResponseDto = shopService.updateShop(id, securityUser, shopUpdateDto);

        return ResponseEntity.ok().body(shopResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShop(
        @PathVariable UUID id,
        @AuthenticationPrincipal SecurityUser securityUser
    ){
        shopService.deleteShop(id, securityUser);

        return ResponseEntity.ok().body(null);
    }
}
