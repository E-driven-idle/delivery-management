package com.driven.dm.review.presentation.controller;

import com.driven.dm.review.application.service.ReviewService;
import com.driven.dm.review.presentation.dto.request.ReviewCreateRequest;
import com.driven.dm.review.presentation.dto.request.ReviewUpdateRequest;
import com.driven.dm.review.presentation.dto.response.ReviewPageResponse;
import com.driven.dm.review.presentation.dto.response.ReviewResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/reviews")
    public ResponseEntity<ReviewResponse> createReview(
        @AuthenticationPrincipal(expression = "id") UUID userId,
        @RequestBody ReviewCreateRequest request) {

        ReviewResponse response = reviewService.createReview(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable UUID reviewId) {
        ReviewResponse response = reviewService.getReview(reviewId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/shops/{shopId}/reviews")
    public ResponseEntity<ReviewPageResponse> getShopReviews(
        @PathVariable UUID shopId,
        @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        ReviewPageResponse response = reviewService.getShopReviews(shopId, pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER','MANAGER')")
    @GetMapping("/users/{userId}/reviews")
    public ResponseEntity<ReviewPageResponse> getUserReviews(
        @AuthenticationPrincipal(expression = "id") UUID loginUserId,
        @PathVariable UUID userId,
        @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {

        if (!loginUserId.equals(userId)) {
        }

        ReviewPageResponse response = reviewService.getUserReviews(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
        @AuthenticationPrincipal(expression = "id") UUID userId,
        @PathVariable UUID reviewId,
        @RequestBody ReviewUpdateRequest request) {

        ReviewResponse response = reviewService.updateReview(userId, reviewId, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('CUSTOMER','MASTER','MANAGER')")
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
        @AuthenticationPrincipal(expression = "id") UUID userId,
        @PathVariable UUID reviewId) {

        reviewService.deleteReview(userId, reviewId);
        return ResponseEntity.noContent().build();
    }
}
