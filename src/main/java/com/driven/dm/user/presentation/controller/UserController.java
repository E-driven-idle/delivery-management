package com.driven.dm.user.presentation.controller;

import com.driven.dm.global.config.security.SecurityUser;
import com.driven.dm.user.application.service.UserService;
import com.driven.dm.user.presentation.dto.ApiUser;
import com.driven.dm.user.presentation.dto.request.UserRoleUpdateRequest;
import com.driven.dm.user.presentation.dto.request.UserUpdateRequest;
import com.driven.dm.user.presentation.dto.response.UserDeleteResponse;
import com.driven.dm.user.presentation.dto.response.UserPageResponse;
import com.driven.dm.user.presentation.dto.response.UserResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PatchMapping("/api/v1/users/me")
    public ResponseEntity<UserDeleteResponse> delete(
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        UUID userId = userService.deleteUser(securityUser.getId());
        return ResponseEntity.ok().body(UserDeleteResponse.from(userId));
    }

    @GetMapping("/api/v1/admin/users")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public ResponseEntity<UserPageResponse> getUsers(
        @RequestParam("page") Long page,
        @RequestParam("pageSize") Long pageSize
    ) {
        UserPageResponse response = userService.getUsers(
            page,
            pageSize);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/v1/admin/users/{userId}")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public ResponseEntity<UserResponse> getUser(
        @PathVariable("userId") UUID userId
    ) {
        UserResponse response = userService.getUserByAdmin(userId);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/api/v1/admin/users/{userId}/role")
    @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
    public ResponseEntity<UserResponse> updateStatus(
        @PathVariable("userId") UUID userId,
        @RequestBody @Valid UserRoleUpdateRequest request,
        @AuthenticationPrincipal SecurityUser securityUser
    ) {
        UserResponse response = userService.updateRole(
            userId,
            request.role(),
            ApiUser.of(securityUser.getId(), securityUser.getRole())
        );

        return ResponseEntity.ok().body(response);
    }
}
