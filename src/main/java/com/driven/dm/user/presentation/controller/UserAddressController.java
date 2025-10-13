package com.driven.dm.user.presentation.controller;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.user.application.service.UserAddressService;
import com.driven.dm.user.presentation.dto.request.UserAddressCreateRequest;
import com.driven.dm.user.presentation.dto.response.UserAddressCreateResponse;
import com.driven.dm.user.presentation.dto.response.UserAddressResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class
UserAddressController {

    private final UserAddressService userAddressService;

    @PostMapping("/api/v1/users/me/addresses")
    public ResponseEntity<UserAddressCreateResponse> createUserAddress(
        @RequestBody @Valid UserAddressCreateRequest request,
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        UUID userAddressId = userAddressService.createUserAddress(securityUser.getId(), request);
        return ResponseEntity.ok().body(UserAddressCreateResponse.of(userAddressId));
    }

    @GetMapping("/api/v1/users/me/addresses")
    public ResponseEntity<List<UserAddressResponse>> getAddresses(
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        List<UserAddressResponse> response = userAddressService.getAddresses(securityUser.getId());
        return ResponseEntity.ok().body(response);
    }
}
