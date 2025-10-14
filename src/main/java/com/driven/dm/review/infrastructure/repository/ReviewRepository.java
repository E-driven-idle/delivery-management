package com.driven.dm.review.infrastructure.repository;

import com.driven.dm.review.domain.entity.Review;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Optional<Review> findByIdAndDeletedAtIsNull(UUID id);

    Page<Review> findByShop_IdAndDeletedAtIsNull(UUID shopId, Pageable pageable);

    Page<Review> findByMenu_IdAndDeletedAtIsNull(UUID menuId, Pageable pageable);

    Page<Review> findByUser_IdAndDeletedAtIsNull(UUID userId, Pageable pageable);


    boolean existsByUser_IdAndShop_IdAndMenuIsNullAndDeletedAtIsNull(UUID userId, UUID shopId);

    boolean existsByUser_IdAndShop_IdAndMenu_IdAndDeletedAtIsNull(UUID userId, UUID shopId,
        UUID menuId);

    @Query("""
        select coalesce(avg(r.rating), 0)
        from Review r
        where r.shop.id = :shopId
          and r.deletedAt is null
        """)
    Double getAvgRatingOfShop(@Param("shopId") UUID shopId);

    Optional<Review> findById(UUID id);
}
