package com.driven.dm.user.presentation.controller;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.user.application.service.UserService;
import com.driven.dm.user.presentation.controller.dto.request.UserUpdateRequest;
import com.driven.dm.user.presentation.controller.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/api/v1/users/me")
    public ResponseEntity<UserResponse> getUser(
        @AuthenticationPrincipal SecurityUser securityUser) {
        UserResponse response = userService.getUser(securityUser.getId());
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/api/v1/users/me")
    public ResponseEntity<UserResponse> updateUser(
        @RequestBody @Valid UserUpdateRequest request,
        @AuthenticationPrincipal SecurityUser securityUser) {
        UserResponse response = userService.updateUser(securityUser.getId(), request);

        return ResponseEntity.ok().body(response);
    }
}
