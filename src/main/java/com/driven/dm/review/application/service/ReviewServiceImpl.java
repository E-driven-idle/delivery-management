package com.driven.dm.review.application.service;

import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.menu.infrastructure.repository.MenuRepository;
import com.driven.dm.review.application.exception.ReviewErrorCode;
import com.driven.dm.review.domain.entity.Review;
import com.driven.dm.review.domain.entity.ReviewImage;
import com.driven.dm.review.infrastructure.repository.ReviewRepository;
import com.driven.dm.review.presentation.dto.request.ReviewCreateRequest;
import com.driven.dm.review.presentation.dto.request.ReviewUpdateRequest;
import com.driven.dm.review.presentation.dto.response.ReviewPageResponse;
import com.driven.dm.review.presentation.dto.response.ReviewResponse;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.shop.infrastructure.repository.ShopJpaRepository;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import com.driven.dm.user.infrastructure.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ShopJpaRepository shopJpaRepository;
    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(UUID userId, ReviewCreateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ReviewErrorCode.REVIEW_NOT_FOUND));

        if (user.getRole() != UserRole.CUSTOMER) {
            throw new AppException(ReviewErrorCode.FORBIDDEN_REVIEW_OWNER);
        }

        Shop shop = shopJpaRepository.findByIdWithMenus(request.getShopId())
            .orElseThrow(() -> new AppException(ReviewErrorCode.REVIEW_NOT_FOUND));

        Menu menu = null;
        if (request.getMenuId() != null) {
            menu = menuRepository.findByIdAndShop_Id(request.getMenuId(), request.getShopId())
                .orElseThrow(() -> new AppException(ReviewErrorCode.REVIEW_NOT_FOUND));
        }

        boolean exists = (menu == null)
            ? reviewRepository.existsByUser_IdAndShop_IdAndMenuIsNullAndDeletedAtIsNull(userId,
            request.getShopId())
            : reviewRepository.existsByUser_IdAndShop_IdAndMenu_IdAndDeletedAtIsNull(userId,
                request.getShopId(), request.getMenuId());
        if (exists) {
            throw new AppException(ReviewErrorCode.DUPLICATE_REVIEW);
        }

        Review review = Review.create(user, shop, menu, request.getContent(), request.getRating());

        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            request.getImageUrls().forEach(url -> review.addImage(ReviewImage.of(url)));
        }

        reviewRepository.saveAndFlush(review);
        return ReviewResponse.from(review);
    }

    @Override
    public ReviewResponse getReview(UUID reviewId) {
        Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
            .orElseThrow(() -> new AppException(ReviewErrorCode.REVIEW_NOT_FOUND));
        return ReviewResponse.from(review);
    }

    @Override
    public ReviewPageResponse getShopReviews(UUID shopId, Pageable pageable) {
        Page<ReviewResponse> page = reviewRepository
            .findByShop_IdAndDeletedAtIsNull(shopId, pageable)
            .map(ReviewResponse::from);
        return ReviewPageResponse.from(page);
    }

    @Override
    public ReviewPageResponse getUserReviews(UUID userId, Pageable pageable) {
        Page<ReviewResponse> page = reviewRepository
            .findByUser_IdAndDeletedAtIsNull(userId, pageable)
            .map(ReviewResponse::from);
        return ReviewPageResponse.from(page);
    }

    @Override
    @Transactional
    public ReviewResponse updateReview(UUID userId, UUID reviewId, ReviewUpdateRequest request) {
        Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
            .orElseThrow(() -> new AppException(ReviewErrorCode.REVIEW_NOT_FOUND));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ReviewErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new AppException(ReviewErrorCode.FORBIDDEN_REVIEW_OWNER);
        }

        review.update(request.getContent(), request.getRating());

        if (request.getImageUrls() != null) {
            review.getImages().forEach(img -> img.delete(userId));
            request.getImageUrls().forEach(url -> review.addImage(ReviewImage.of(url)));
        }

        reviewRepository.saveAndFlush(review);
        return ReviewResponse.from(review);
    }

    @Override
    @Transactional
    public void deleteReview(UUID userId, UUID reviewId) {
        Review review = reviewRepository.findByIdAndDeletedAtIsNull(reviewId)
            .orElseThrow(() -> new AppException(ReviewErrorCode.REVIEW_NOT_FOUND));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new AppException(ReviewErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getId().equals(user.getId()) && !user.getRole().isAdmin()) {
            throw new AppException(ReviewErrorCode.FORBIDDEN_REVIEW_OWNER);
        }

        review.delete(userId);
    }
}
