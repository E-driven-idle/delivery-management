package com.driven.dm.user.presentation.controller;

import com.driven.dm.user.application.service.AuthService;
import com.driven.dm.user.presentation.dto.request.UserCreateRequest;
import com.driven.dm.user.presentation.dto.response.UserCreateResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/v1/auth/sign-up")
    public ResponseEntity<UserCreateResponse> signUp(
        @RequestBody @Valid UserCreateRequest request) {
        UUID userId = authService.signUp(request);
        return ResponseEntity.ok().body(UserCreateResponse.of(userId));
    }

}
