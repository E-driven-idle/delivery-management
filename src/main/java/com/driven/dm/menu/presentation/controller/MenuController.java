package com.driven.dm.menu.presentation.controller;

import com.driven.dm.menu.application.service.MenuService;
import com.driven.dm.menu.presentation.dto.request.MenuCreateDto;
import com.driven.dm.menu.presentation.dto.response.MenuCreateResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/{id}")
    public ResponseEntity<MenuCreateResponse> createMenu(@PathVariable UUID id, MenuCreateDto menuCreateDto){

        MenuCreateResponse menuCreateResponse = menuService.createMenu(id, menuCreateDto);

        return ResponseEntity.ok().body(menuCreateResponse);
    }

}
