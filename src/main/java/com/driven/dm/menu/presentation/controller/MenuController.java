package com.driven.dm.menu.presentation.controller;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.menu.application.service.MenuService;
import com.driven.dm.menu.presentation.dto.request.MenuCreateRequest;
import com.driven.dm.menu.presentation.dto.request.MenuUpdateRequest;
import com.driven.dm.menu.presentation.dto.response.MenuCreateResponse;
import com.driven.dm.menu.presentation.dto.response.MenuListResponse;
import com.driven.dm.menu.presentation.dto.response.MenuUpdateResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuListResponse>> menuList(
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        List<MenuListResponse> menuListResponses = menuService.menuList(securityUser);

        return ResponseEntity.ok().body(menuListResponses);
    }

    @PostMapping("/{id}")
    public ResponseEntity<MenuCreateResponse> createMenu(@PathVariable UUID id, @RequestBody MenuCreateRequest menuCreateRequest){

        MenuCreateResponse menuCreateResponse = menuService.createMenu(id, menuCreateRequest);

        return ResponseEntity.ok().body(menuCreateResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuUpdateResponse> updateMenu(
        @PathVariable UUID id,
        @AuthenticationPrincipal SecurityUser securityUser,
        @RequestBody MenuUpdateRequest menuUpdateRequest
    ){
        MenuUpdateResponse menuUpdateResponse = menuService.updateMenu(id, securityUser, menuUpdateRequest);

        return ResponseEntity.ok().body(menuUpdateResponse);
    }

}
