package com.driven.dm.shop.presentation.controller;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.shop.application.service.ShopAddressService;
import com.driven.dm.shop.application.service.ShopService;
import com.driven.dm.shop.domain.entity.ShopCategory;
import com.driven.dm.shop.presentation.dto.request.ShopAddressCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopAddressUpdateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopCreateRequest;
import com.driven.dm.shop.presentation.dto.request.ShopUpdateRequest;
import com.driven.dm.shop.presentation.dto.response.AdminShopListResponse;
import com.driven.dm.shop.presentation.dto.response.ShopAddressResponse;
import com.driven.dm.shop.presentation.dto.response.ShopCreateResponse;
import com.driven.dm.shop.presentation.dto.response.ShopListResponse;
import com.driven.dm.shop.presentation.dto.response.ShopResponse;
import com.driven.dm.shop.presentation.dto.response.ShopUpdateResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<Page<ShopListResponse>> searchByShopName (
        @RequestParam("shopName") String shopName,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "DESC")Sort.Direction direction
        )
    {
        Page<ShopListResponse> shopListResponse = shopService.searchByShopName(shopName, page, size, direction);
        return ResponseEntity.ok().body(shopListResponse);
    }

    @GetMapping("/category")
    public ResponseEntity<Page<ShopListResponse>> searchByCategory(
        @RequestParam("category")ShopCategory category,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Page<ShopListResponse> shopListResponse = shopService.searchByCategory(category, page, size, direction);
        return ResponseEntity.ok().body(shopListResponse);
    }

    @GetMapping
    public ResponseEntity<Page<ShopListResponse>> shopList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ){
        Page<ShopListResponse> shopListResponse = shopService.shopList(page, size, direction);

        return ResponseEntity.ok().body(shopListResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShopResponse> selectShop(@PathVariable UUID id) {
        ShopResponse shopResponse = shopService.selectShop(id);

        return ResponseEntity.ok().body(shopResponse);
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

    @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
    @GetMapping("/admin")
    public ResponseEntity<Page<AdminShopListResponse>>  searchByAdmin(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Page<AdminShopListResponse> shopListResponses = shopService.adminShopList(page, size, direction);
        return  ResponseEntity.ok().body(shopListResponses);
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

    @DeleteMapping("/{id}/address")
    public ResponseEntity<Void> deleteAddress(
        @PathVariable UUID id,
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        shopAddressService.deleteAddress(id, securityUser);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
