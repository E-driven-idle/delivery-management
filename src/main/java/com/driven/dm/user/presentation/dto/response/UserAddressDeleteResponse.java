package com.driven.dm.user.presentation.dto.response;

import java.util.UUID;

public record UserAddressDeleteResponse(
    UUID userAddressId
){

    public static UserAddressDeleteResponse from(UUID id) {
        return new UserAddressDeleteResponse(id);
    }
}
