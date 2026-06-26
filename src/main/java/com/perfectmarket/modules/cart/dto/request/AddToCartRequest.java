package com.perfectmarket.modules.cart.dto.request;
import java.util.UUID;

public record AddToCartRequest(
        UUID productId,
        Integer quantity
) { }