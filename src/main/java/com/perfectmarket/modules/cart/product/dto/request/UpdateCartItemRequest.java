package com.perfectmarket.modules.cart.product.dto.request;

import java.util.UUID;

public record UpdateCartItemRequest(
        UUID productId,
        Integer quantity
) {}
