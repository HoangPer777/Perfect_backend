package com.perfectmarket.modules.product_order.service.impl;

import com.perfectmarket.modules.product.Product;
import com.perfectmarket.modules.product.ProductRepository;
import com.perfectmarket.modules.product_order.dto.request.OrderCreateRequest;
import com.perfectmarket.modules.product_order.dto.response.ProductOrderHistoryResponse;
import com.perfectmarket.modules.product_order.entity.Order;
import com.perfectmarket.modules.product_order.entity.OrderProductItem;
import com.perfectmarket.modules.product_order.enums.OrderStatus;
import com.perfectmarket.modules.product_order.repository.OrderProductItemRepository;
import com.perfectmarket.modules.product_order.repository.OrderRepository;
import com.perfectmarket.modules.product_order.service.OrderService;
import com.perfectmarket.modules.service.ServicePackageRepository;
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
    private final ServicePackageRepository servicePackageRepository;
    private final com.perfectmarket.modules.review.ReviewRepository reviewRepository;

//    @Transactional
//    public UUID createOrderFromCart(UUID userId, List<UUID> productIds) {
//        List<Product> products = productRepository.findAllById(productIds);
//
//        BigDecimal total = BigDecimal.ZERO;
//        Order order = Order.builder()
//                .customerId(userId)
//                .totalPrice(BigDecimal.ZERO)
//                .status(OrderStatus.NOT_PAID)
//                .items(new ArrayList<>())
//                .build();
//
//        for (Product product : products) {
//            OrderProductItem item = OrderProductItem.builder()
//                    .order(order)
//                    .productId(product.getId())
//                    .productTitle(product.getTitle())
//                    .priceAtPurchase(product.getPrice())
//                    .quantity(1)
//                    .build();
//
//            order.addOrderItem(item);
//            total = total.add(product.getPrice());
//        }
//
//        order.setTotalPrice(total);
//
//        Order savedOrder = orderRepository.save(order);
//
//        return savedOrder.getId();
//    }


    @Transactional
    @Override
    public UUID createOrderFromCart(UUID userId, OrderCreateRequest request) {
        BigDecimal total = BigDecimal.ZERO;
        Order order = Order.builder()
                .customerId(userId)
                .status(OrderStatus.NOT_PAID)
                .items(new ArrayList<>())
                .build();

        for (UUID productId : request.productIds()) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Không tìm thấy sản phẩm: " + productId));

            BigDecimal priceAtPurchase = product.getPrice();


            OrderProductItem item = OrderProductItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .productTitle(product.getTitle())
                    .priceAtPurchase(priceAtPurchase != null ? priceAtPurchase : BigDecimal.ZERO)
                    .quantity(1)
                    .build();

            order.addOrderItem(item);
            total = total.add(item.getPriceAtPurchase());
        }

        order.setTotalPrice(total);
        return orderRepository.save(order).getId();
    }

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

        List<com.perfectmarket.modules.review.Review> userReviews = reviewRepository.findByReviewerId(userId);
        Map<UUID, com.perfectmarket.modules.review.Review> reviewMap = userReviews.stream()
                .filter(r -> r.getProduct() != null)
                .collect(Collectors.toMap(r -> r.getProduct().getId(), r -> r, (existing, replacement) -> existing));

        return orders.stream().map(order ->
                new ProductOrderHistoryResponse(
                        order.getId(),
                        order.getCreatedAt(),
                        order.getItems().stream().map(item -> {
                            boolean isReviewed = reviewMap.containsKey(item.getProductId());
                            Integer rating = isReviewed ? reviewMap.get(item.getProductId()).getRating() : null;
                            return new ProductOrderHistoryResponse.OrderItemDto(
                                    item.getId(),
                                    item.getProductId(),
                                    item.getProductTitle(),
                                    productMap.containsKey(item.getProductId())
                                            ? productMap.get(item.getProductId()).getThumbnailUrl()
                                            : "/default-thumbnail.png",
                                    item.getPriceAtPurchase(),
                                    isReviewed,
                                    rating
                            );
                        }).toList()
                )
        ).toList();
    }

    @Override
    public String getDownloadUrlForProduct(UUID userId, UUID orderItemId) {
        OrderProductItem item = orderProductItemRepository.findById(orderItemId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Không tìm thấy mục đơn hàng"));

        if (!item.getOrder().getCustomerId().equals(userId)) {
            throw new SecurityException("Bạn không có quyền truy cập sản phẩm này.");
        }
        return orderProductItemRepository.findThumbnailUrlByProductId(item.getProductId())
                .orElse("/default-thumbnail.png");
    }
}