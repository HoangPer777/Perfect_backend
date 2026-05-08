package com.perfectmarket.modules.product.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CreateProductResponse {
    public record ProductImageResponse(UUID id, String url) {}
    public record ProductResponse(UUID id, String title, String description, BigDecimal price, String thumbnailUrl, String status,
                                  int viewCount, int soldCount, double ratingAgv, List<ProductImageResponse> images, LocalDateTime createdAt, LocalDateTime updatedAt) {
    }

}

