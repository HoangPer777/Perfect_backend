package com.perfectmarket.modules.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByReviewerId(UUID reviewerId);
    List<Review> findByProductId(UUID productId);
    Optional<Review> findByReviewerIdAndProductId(UUID reviewerId, UUID productId);
}
