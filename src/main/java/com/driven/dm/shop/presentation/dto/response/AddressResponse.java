package com.driven.dm.shop.presentation.dto.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {

    private UUID id;
    private String fullAddress;
    private String region_1depth_name;
    private String region_2depth_name;
    private String region_3depth_name;
    private String h_code;
    private Double latitude;
    private Double longitude;
    private String source;

}
