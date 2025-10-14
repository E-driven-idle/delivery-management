package com.driven.dm.review.presentation.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewImageResponse {

    private UUID id;
    private String imageUrl;
}
