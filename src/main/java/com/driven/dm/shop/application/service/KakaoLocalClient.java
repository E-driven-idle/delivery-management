package com.driven.dm.shop.application.service;

import com.driven.dm.shop.presentation.dto.response.KakaoAddressSearchResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class KakaoLocalClient {

    private final RestClient restClient;

    @Value("${kakao.local.rest-api-key}")
    private String apiKey;

    public Optional<KakaoAddressSearchResponse.Document> searchFirst(String query){
        KakaoAddressSearchResponse body = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/v2/local/search/address.json")
                .queryParam("query", query)
                .build())
            .header("Authorization", "KakaoAK " + apiKey)
            .retrieve()
            .body(KakaoAddressSearchResponse.class);

        if(body == null || body.getDocuments() == null || body.getDocuments().isEmpty()){
            return Optional.empty();
        }

        return Optional.of(body.getDocuments().get(0));
    }
}