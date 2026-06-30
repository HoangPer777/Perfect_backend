package com.perfectmarket.modules.review;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.product.Product;
import com.perfectmarket.modules.product.ProductRepository;
import com.perfectmarket.modules.product_order.repository.OrderProductItemRepository;
import com.perfectmarket.modules.product_order.enums.OrderStatus;
import com.perfectmarket.modules.review.dto.CreateReviewRequest;
import com.perfectmarket.modules.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderProductItemRepository orderProductItemRepository;

    @Transactional
    public ReviewResponse createProductReview(UUID reviewerId, CreateReviewRequest request) {
        UUID productId = request.productId();

//        boolean purchased = orderProductItemRepository.hasUserPurchasedProduct(reviewerId, productId, OrderStatus.COMPLETED);
//        if (!purchased) {
//            throw new IllegalStateException("Bạn chỉ có thể đánh giá sản phẩm sau khi đã mua thành công!");
//        }

        // 2. Kiểm tra xem đã đánh giá sản phẩm này chưa
        if (reviewRepository.findByReviewerIdAndProductId(reviewerId, productId).isPresent()) {
            throw new IllegalStateException("Bạn đã đánh giá sản phẩm này rồi!");
        }

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng: " + reviewerId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm: " + productId));

        // 3. Tạo mới đánh giá
        Review review = Review.builder()
                .reviewer(reviewer)
                .product(product)
                .rating(request.rating())
                .content(request.content())
                .reason(request.reason())
                .build();

        Review savedReview = reviewRepository.save(review);

        // 4. Tính toán lại điểm đánh giá trung bình của sản phẩm
        List<Review> productReviews = reviewRepository.findByProductId(productId);
        double sum = productReviews.stream().mapToDouble(Review::getRating).sum();
        double avg = productReviews.isEmpty() ? 0.0 : sum / productReviews.size();

        double roundedAvg = Math.round(avg * 100.0) / 100.0;
        product.setRatingAvg(roundedAvg);
        productRepository.save(product);

        // 5. Trả về DTO kết quả
        return new ReviewResponse(
                savedReview.getId(),
                reviewer.getId(),
                reviewer.getFullName() != null ? reviewer.getFullName() : reviewer.getUsername(),
                reviewer.getAvatarUrl(),
                product.getId(),
                savedReview.getRating(),
                savedReview.getContent(),
                savedReview.getReason()
        );
    }
}
