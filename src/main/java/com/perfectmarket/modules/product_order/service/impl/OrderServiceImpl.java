package com.perfectmarket.modules.product_order.service.impl;

import com.perfectmarket.modules.product.Product;
import com.perfectmarket.modules.product.ProductRepository;
import com.perfectmarket.modules.product_order.dto.response.ProductOrderHistoryResponse;
import com.perfectmarket.modules.product_order.entity.Order;
import com.perfectmarket.modules.product_order.entity.OrderProductItem;
import com.perfectmarket.modules.product_order.enums.OrderStatus;
import com.perfectmarket.modules.product_order.repository.OrderProductItemRepository;
import com.perfectmarket.modules.product_order.repository.OrderRepository;
import com.perfectmarket.modules.product_order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderProductItemRepository orderProductItemRepository;

    @Transactional
    public UUID createOrderFromCart(UUID userId, List<UUID> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        BigDecimal total = BigDecimal.ZERO;
        Order order = Order.builder()
                .customerId(userId)
                .totalPrice(BigDecimal.ZERO)
                .status(OrderStatus.NOT_PAID)
                .items(new ArrayList<>())
                .build();

        for (Product product : products) {
            OrderProductItem item = OrderProductItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .productTitle(product.getTitle())
                    .priceAtPurchase(product.getPrice())
                    .quantity(1)
                    .build();

            order.addOrderItem(item);
            total = total.add(product.getPrice());
        }

        order.setTotalPrice(total);

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

    // Trong OrderService.java
    @Transactional(readOnly = true)
    public List<ProductOrderHistoryResponse> getProductOrderHistory(UUID userId) {
        List<Order> orders = orderRepository.findByCustomerIdOrderByCreatedAtDesc(userId);

        Set<UUID> allProductIds = orders.stream()
                .flatMap(o -> o.getItems().stream())
                .map(OrderProductItem::getProductId)
                .collect(Collectors.toSet());

        Map<UUID, Product> productMap = productRepository.findAllById(allProductIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        return orders.stream().map(order ->
                new ProductOrderHistoryResponse(
                        order.getId(),
                        order.getCreatedAt(),
                        order.getItems().stream().map(item ->
                                new ProductOrderHistoryResponse.OrderItemDto(
                                        item.getId(),
                                        item.getProductTitle(),
                                        productMap.containsKey(item.getProductId())
                                                ? productMap.get(item.getProductId()).getThumbnailUrl()
                                                : "/default-thumbnail.png",
                                        item.getPriceAtPurchase()
                                )
                        ).toList()
                )
        ).toList();
    }
}