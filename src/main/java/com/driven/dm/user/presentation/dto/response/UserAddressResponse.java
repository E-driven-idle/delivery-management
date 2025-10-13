package com.driven.dm.user.presentation.dto.response;

import com.driven.dm.user.domain.entity.UserAddress;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserAddressResponse(
    UUID id,
    String zipCode,
    String primaryAddress,
    String detailAddress,
    boolean isDefault
) {

    public static UserAddressResponse from(UserAddress userAddress) {
        return UserAddressResponse.builder()
            .id(userAddress.getId())
            .zipCode(userAddress.getAddress().getZipCode())
            .primaryAddress(userAddress.getAddress().getPrimaryAddress())
            .detailAddress(userAddress.getAddress().getDetailAddress())
            .isDefault(userAddress.isDefault())
            .build();
    }
}
