package com.perfectmarket.modules.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProductDetailResponse(
        UUID id,
        DesignerResponse designer,
        String title,
        String description,
        BigDecimal price,
        String thumbnailUrl,
        Integer viewCount,
        Integer soldCount,
        Double ratingAvg,
        List<ImageResponse> images,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record DesignerResponse(UUID id, String email, String username, String avatarUrl) {}
    public record ImageResponse(UUID id, String url) {}
}