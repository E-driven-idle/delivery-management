package com.driven.dm.user.presentation.dto.response;

import java.util.List;

public record UserPageResponse(
    List<UserResponse> users,
    Long count
) {
    public static UserPageResponse of(List<UserResponse> users, Long count) {
        return new UserPageResponse(users, count);
    }
}
