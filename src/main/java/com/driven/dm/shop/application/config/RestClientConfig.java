package com.driven.dm.shop.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient kakaoRestClient(RestClient.Builder builder) {

        return builder
            .baseUrl("https://dapi.kakao.com")
            .build();
    }

}
