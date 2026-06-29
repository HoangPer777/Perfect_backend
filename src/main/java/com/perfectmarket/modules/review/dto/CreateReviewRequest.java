package com.perfectmarket.modules.review.dto;

import java.util.UUID;

public record CreateReviewRequest(
    UUID productId,
    int rating,
    String content,
    String reason
) {}
