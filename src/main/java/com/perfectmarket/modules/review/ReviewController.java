package com.perfectmarket.modules.review;

import com.perfectmarket.modules.auth.UserPrincipal;
import com.perfectmarket.modules.review.dto.CreateReviewRequest;
import com.perfectmarket.modules.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/product")
    public ResponseEntity<ReviewResponse> createProductReview(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody CreateReviewRequest request) {
        ReviewResponse response = reviewService.createProductReview(principal.id(), request);
        return ResponseEntity.ok(response);
    }
}
