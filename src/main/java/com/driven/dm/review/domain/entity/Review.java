package com.driven.dm.review.domain.entity;

import com.driven.dm.global.entity.BaseEntity;
import com.driven.dm.global.exception.AppException;
import com.driven.dm.menu.domain.entity.Menu;
import com.driven.dm.review.application.exception.ReviewErrorCode;
import com.driven.dm.shop.domain.entity.Shop;
import com.driven.dm.user.domain.entity.User;
import com.driven.dm.user.domain.entity.UserRole;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_review")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "writer_role", nullable = false, length = 20)
    private UserRole writerRole;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "content", length = 255, nullable = false)
    private String content;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> images = new ArrayList<>();

    public static Review create(User user, Shop shop, Menu menu, String content, Integer rating) {
        validateUserRole(user);
        validateRating(rating);

        Review r = new Review();
        r.user = user;
        r.writerRole = user.getRole();
        r.shop = shop;
        r.menu = menu;
        r.content = content;
        r.rating = rating;
        return r;
    }

    public void update(String newContent, Integer newRating) {
        if (newContent != null && !newContent.isBlank()) {
            this.content = newContent;
        }
        if (newRating != null) {
            validateRating(newRating);
            this.rating = newRating;
        }
    }

    private static void validateUserRole(User user) {
        if (user.getRole() != UserRole.CUSTOMER) {
            throw new AppException(ReviewErrorCode.FORBIDDEN_REVIEW_OWNER);
        }
    }

    private static void validateRating(Integer rating) {
        if (rating == null || rating < 1 || rating > 5) {
            throw new AppException(ReviewErrorCode.INVALID_RATING_VALUE);
        }
    }

    // ✅ 반드시 이 메서드를 통해 추가 (FK 세팅)
    public void addImage(ReviewImage image) {
        image.setReview(this);
        this.images.add(image);
    }
}
