package com.driven.dm.user.presentation.dto.response;

import java.util.UUID;

public record UserDeleteResponse(
    UUID userId
) {
    public static UserDeleteResponse from(UUID id) {
        return new UserDeleteResponse(id);
    }
}
