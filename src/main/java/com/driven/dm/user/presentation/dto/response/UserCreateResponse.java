package com.driven.dm.user.presentation.dto.response;

import java.util.UUID;

public record UserCreateResponse(
    UUID userId
) {
    public static UserCreateResponse of(UUID userId) {
        return new UserCreateResponse(userId);
    }
}