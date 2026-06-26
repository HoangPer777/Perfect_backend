package com.perfectmarket.modules.product_order.service;

import com.perfectmarket.modules.product_order.dto.response.ProductOrderHistoryResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    UUID createOrderFromCart(UUID userId, List<UUID> productIds);

    List<ProductOrderHistoryResponse> getProductOrderHistory(UUID userId);
}
