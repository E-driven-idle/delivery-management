package com.driven.dm.shop.presentation.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class KakaoAddressSearchResponse {

    private List<Document> documents;

    @Getter @Setter
    public static class Document{
        private Address address;
    }

    @Getter @Setter
    public static class Address{
        private String address_name;
        private String region_1depth_name;
        private String region_2depth_name;
        private String region_3depth_name;
        private String h_code;
        private String x;
        private String y;
    }
}