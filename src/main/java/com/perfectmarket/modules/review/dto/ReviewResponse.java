package com.perfectmarket.modules.review.dto;

import java.util.UUID;

public record ReviewResponse(
    UUID id,
    UUID reviewerId,
    String reviewerName,
    String reviewerAvatar,
    UUID productId,
    int rating,
    String content,
    String reason
) {}
