package com.perfectmarket.modules.product_order.service;

import com.perfectmarket.modules.product_order.dto.request.OrderCreateRequest;
import com.perfectmarket.modules.product_order.dto.response.ProductOrderHistoryResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    //    UUID createOrderFromCart(UUID userId, List<UUID> productIds);
    UUID createOrderFromCart(UUID userId, OrderCreateRequest request);

    List<ProductOrderHistoryResponse> getProductOrderHistory(UUID userId);
}
