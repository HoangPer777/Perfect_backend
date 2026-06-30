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
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequest request) {
        try {
            UUID userId = currentUserProvider.getCurrentUserId();
            if (userId == null) {
                return ResponseEntity.status(401).body("User không hợp lệ");
            }

            UUID orderId = orderService.createOrderFromCart(userId, request);
            return ResponseEntity.ok(Map.of("orderId", orderId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }
    @GetMapping("/download-link/{orderItemId}")
    public ResponseEntity<Map<String, String>> getDownloadLink(@PathVariable UUID orderItemId) {
        UUID userId = currentUserProvider.getCurrentUserId();
        String downloadUrl = orderService.getDownloadUrlForProduct(userId, orderItemId);

        return ResponseEntity.ok(Map.of("downloadUrl", downloadUrl));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ProductOrderHistoryResponse>> getOrderHistory() {
        UUID userId = currentUserProvider.getCurrentUserId();
        List<ProductOrderHistoryResponse> history = orderService.getProductOrderHistory(userId);
        return ResponseEntity.ok(history);
    }
}