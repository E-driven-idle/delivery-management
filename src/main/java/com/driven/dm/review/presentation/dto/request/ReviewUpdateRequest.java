package com.driven.dm.review.presentation.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {

    @Size(max = 255, message = "content는 최대 255자까지 가능합니다.")
    private String content;

    @Min(value = 1, message = "rating은 1 이상이어야 합니다.")
    @Max(value = 5, message = "rating은 5 이하여야 합니다.")
    private Integer rating;

    private List<@NotBlank String> imageUrls;
}
