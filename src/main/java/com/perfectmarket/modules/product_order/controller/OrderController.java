package com.perfectmarket.modules.product_order.controller;

import com.perfectmarket.modules.auth.security.CurrentUserProvider;
import com.perfectmarket.modules.product_order.dto.request.OrderCreateRequest; // Bạn cần tạo DTO này
import com.perfectmarket.modules.product_order.dto.response.ProductOrderHistoryResponse;
import com.perfectmarket.modules.product_order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserProvider currentUserProvider;

    //    @PostMapping("/create")
//    public ResponseEntity<Map<String, UUID>> createOrder(@RequestBody OrderCreateRequest request) {
//        UUID userId = currentUserProvider.getCurrentUserId();
//        UUID orderId = orderService.createOrderFromCart(userId, request.productIds());
//
//        return ResponseEntity.ok(Map.of("orderId", orderId));
//    }
    @PostMapping("/create")
    public ResponseEntity<Map<String, UUID>> createOrder(@RequestBody OrderCreateRequest request) {
        UUID userId = currentUserProvider.getCurrentUserId();
        UUID orderId = orderService.createOrderFromCart(userId, request);

        return ResponseEntity.ok(Map.of("orderId", orderId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ProductOrderHistoryResponse>> getOrderHistory() {
        UUID userId = currentUserProvider.getCurrentUserId();
        List<ProductOrderHistoryResponse> history = orderService.getProductOrderHistory(userId);
        return ResponseEntity.ok(history);
    }
}