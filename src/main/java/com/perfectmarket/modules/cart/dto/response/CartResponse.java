package com.perfectmarket.modules.cart.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID cartId,
        List<CartItemResponse> items,
        BigDecimal totalPrice
) {
    public record CartItemResponse(
            UUID cartItemId,
            UUID productId,
            String productTitle,
            String thumbnailUrl,
            BigDecimal price,
            Integer quantity
    ) {}
}