package com.driven.dm.shop.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddressCreateRequest {

    @NotBlank
    private String query;

}
