package com.driven.dm.user.presentation.dto.response;


import java.util.UUID;

public record UserAddressCreateResponse(
    UUID userAddressId
) {
    public static UserAddressCreateResponse of(UUID id) {
        return new UserAddressCreateResponse(id);
    }
}
