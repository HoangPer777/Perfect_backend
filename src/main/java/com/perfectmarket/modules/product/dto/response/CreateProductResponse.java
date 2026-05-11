package com.perfectmarket.modules.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateProductResponse(UUID id, String title, String description, BigDecimal price, String thumbnailUrl, String status,
                                    int viewCount, int soldCount, double ratingAgv, List<ProductImageResponse> images, List<CategoryResponse> categories,
                                    LocalDateTime createdAt, LocalDateTime updatedAt) {
    public record ProductImageResponse(UUID id, String url) {}
    public record CategoryResponse(UUID id, String name) {}
}

