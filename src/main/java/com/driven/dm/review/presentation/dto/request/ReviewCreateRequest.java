package com.driven.dm.review.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewCreateRequest {

    @NotNull(message = "shopId는 필수입니다.")
    private UUID shopId;

    private UUID menuId;

    @NotBlank(message = "content는 비어있을 수 없습니다.")
    @Size(max = 255, message = "content는 최대 255자까지 가능합니다.")
    private String content;

    @NotNull(message = "rating은 필수입니다.")
    @Min(value = 1, message = "rating은 1 이상이어야 합니다.")
    @Max(value = 5, message = "rating은 5 이하여야 합니다.")
    private Integer rating;

    @Size(max = 5, message = "이미지는 최대 5개까지 업로드 가능합니다.")
    private List<@NotBlank String> imageUrls;
}
