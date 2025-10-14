package com.driven.dm.review.presentation.dto.response;

import com.driven.dm.review.domain.entity.Review;
import com.driven.dm.review.domain.entity.ReviewImage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewResponse {

    private UUID id;

    private UUID userId;
    private String nickname;
    private String writerRole; // CUSTOMER / OWNER / ADMIN

    private UUID shopId;
    private UUID menuId;

    private String content;
    private Integer rating;

    private List<ReviewImageResponse> images;

    private LocalDateTime createdAt;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
            .id(review.getId())
            .userId(review.getUser().getId())
            .nickname(review.getUser().getNickname())
            .writerRole(review.getWriterRole().name())
            .shopId(review.getShop().getId())
            .menuId(review.getMenu() != null ? review.getMenu().getId() : null)
            .content(review.getContent())
            .rating(review.getRating())
            .images(toImageResponses(review.getImages()))
            .createdAt(review.getCreatedAt())
            .build();
    }

    private static List<ReviewImageResponse> toImageResponses(List<ReviewImage> images) {
        if (images == null) {
            return List.of();
        }
        return images.stream()
            .filter(img -> img.getDeletedAt() == null)
            .map(img -> ReviewImageResponse.builder()
                .id(img.getId())
                .imageUrl(img.getImageUrl())
                .build())
            .collect(Collectors.toList());
    }
}
