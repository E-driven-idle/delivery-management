package com.driven.dm.user.presentation.controller.dto.response;

import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserResponse(
    UUID id,
    String username,
    String nickName,
    UserRole role
) {

    public static UserResponse from(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .nickName(user.getNickname())
            .build();
    }
}
