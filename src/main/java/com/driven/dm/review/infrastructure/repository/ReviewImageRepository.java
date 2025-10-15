package com.driven.dm.review.infrastructure.repository;

import com.driven.dm.review.domain.entity.ReviewImage;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, UUID> {

    List<ReviewImage> findByReview_IdAndDeletedAtIsNull(UUID reviewId);
}
