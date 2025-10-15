package com.driven.dm.review.application.service;

import com.driven.dm.review.presentation.dto.request.ReviewCreateRequest;
import com.driven.dm.review.presentation.dto.request.ReviewUpdateRequest;
import com.driven.dm.review.presentation.dto.response.ReviewPageResponse;
import com.driven.dm.review.presentation.dto.response.ReviewResponse;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface ReviewService {

    ReviewResponse createReview(UUID userId, ReviewCreateRequest request);

    ReviewResponse getReview(UUID reviewId);

    ReviewPageResponse getShopReviews(UUID shopId, Pageable pageable);

    ReviewPageResponse getUserReviews(UUID userId, Pageable pageable);

    ReviewResponse updateReview(UUID userId, UUID reviewId, ReviewUpdateRequest request);

    void deleteReview(UUID userId, UUID reviewId);
}
