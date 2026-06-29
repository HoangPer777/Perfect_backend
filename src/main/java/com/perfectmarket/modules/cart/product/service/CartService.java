package com.perfectmarket.modules.cart.product.service;

import com.perfectmarket.modules.cart.product.dto.request.AddToCartRequest;
import com.perfectmarket.modules.cart.product.dto.request.UpdateCartItemRequest;
import com.perfectmarket.modules.cart.product.dto.response.CartResponse;

import java.util.UUID;

public interface CartService {
    void addToCart(UUID userId, AddToCartRequest request);
    void removeItem(UUID userId, UUID productId);
    void clearCart(UUID userId);
    CartResponse getCart(UUID userId);

    void updateItemQuantity(UUID userId, UpdateCartItemRequest request);
}
