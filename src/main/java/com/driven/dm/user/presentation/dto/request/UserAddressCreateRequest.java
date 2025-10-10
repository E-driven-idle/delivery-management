package com.driven.dm.user.presentation.dto.request;

public record UserAddressCreateRequest(
    String zipCode,
    String primaryAddress,
    String detailAddress
) {

}
