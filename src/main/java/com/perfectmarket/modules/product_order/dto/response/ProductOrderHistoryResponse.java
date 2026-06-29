package com.perfectmarket.modules.product_order.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProductOrderHistoryResponse(
        UUID orderId,
        LocalDateTime purchaseDate,
        List<OrderItemDto> items
) {
    public record OrderItemDto(
            UUID orderItemId,
            UUID productId,
            String productTitle,
            String thumbnailUrl,
            BigDecimal priceAtPurchase,
            Boolean isReviewed,
            Integer rating
    ) {}
}