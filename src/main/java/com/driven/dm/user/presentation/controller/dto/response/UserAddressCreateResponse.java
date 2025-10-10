package com.driven.dm.user.presentation.controller.dto.response;


import java.util.UUID;

public record UserAddressCreateResponse(
    UUID userAddressId
) {
    public static UserAddressCreateResponse of(UUID id) {
        return new UserAddressCreateResponse(id);
    }
}
