package com.driven.dm.user.presentation.dto;

import com.driven.dm.user.domain.entity.UserRole;
import java.util.UUID;

public record ApiUser(
    UUID userId,
    UserRole role
) {
    public static ApiUser of(UUID userId, UserRole role) {
        return new ApiUser(userId, role);
    }
}
