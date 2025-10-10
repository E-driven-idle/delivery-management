package com.driven.dm.user.presentation.controller.dto.request;

public record UserAddressCreateRequest(
    String zipCode,
    String primaryAddress,
    String detailAddress
) {

}
