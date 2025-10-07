package com.driven.dm.shop.presentation.controller;

import com.driven.dm.shop.application.service.ShopService;
import com.driven.dm.shop.presentation.dto.request.ShopDto;
import com.driven.dm.shop.presentation.dto.response.ShopListResponseDto;
import com.driven.dm.shop.presentation.dto.response.ShopResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody ShopDto shopDto){

        ShopResponseDto shopResponseDto = shopService.createShop(userDetails, shopDto);

        return ResponseEntity.ok().body(shopResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ShopListResponseDto>> getShop(){
        List<ShopListResponseDto> shopListResponseDto = shopService.getShop();

        return ResponseEntity.ok().body(shopListResponseDto);
    }
}
