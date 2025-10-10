package com.driven.dm.user.domain.rule;

import com.driven.dm.user.domain.entity.UserRole;
import java.util.UUID;

public record UserIdAndRole(
    UUID userId,
    UserRole role
) {

    public static UserIdAndRole from(UUID userId, UserRole role) {
        return new UserIdAndRole(userId, role);
    }
}
