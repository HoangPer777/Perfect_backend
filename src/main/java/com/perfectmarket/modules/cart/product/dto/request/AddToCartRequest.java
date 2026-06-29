package com.perfectmarket.modules.cart.product.dto.request;
import java.util.UUID;

public record AddToCartRequest(
        UUID productId,
        Integer quantity
) { }