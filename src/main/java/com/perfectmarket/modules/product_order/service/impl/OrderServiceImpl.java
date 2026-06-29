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
                .totalPrice(BigDecimal.ZERO)
                .status(OrderStatus.NOT_PAID)
                .items(new ArrayList<>())
                .build();

        // Duyệt trực tiếp qua mảng productIds từ request để đảm bảo đúng thứ tự index
        for (int i = 0; i < request.productIds().size(); i++) {
            UUID productId = request.productIds().get(i);

            // Tìm kiếm thực thể Product trong Database
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + productId));

            BigDecimal priceAtPurchase = product.getPrice(); // Mặc định lấy giá sản phẩm thường

            // KIỂM TRA: Nếu tại index này có chứa ID gói dịch vụ
            if (request.servicePackageIds() != null && i < request.servicePackageIds().size()) {
                UUID spId = request.servicePackageIds().get(i);
                if (spId != null) {
                    // Lấy giá chuẩn của gói dịch vụ cụ thể (BASIC/PRO/VIP) thế vào giá mua
                    var servicePackageOpt = servicePackageRepository.findById(spId);
                    if (servicePackageOpt.isPresent()) {
                        priceAtPurchase = servicePackageOpt.get().getPrice();
                    }
                }
            }

            // Khử hoàn toàn lỗi NullPointerException đề phòng dữ liệu cha/con bị thiếu giá
            if (priceAtPurchase == null) {
                priceAtPurchase = BigDecimal.ZERO;
            }

            OrderProductItem item = OrderProductItem.builder()
                    .order(order)
                    .productId(product.getId())
                    .productTitle(product.getTitle())
                    .priceAtPurchase(priceAtPurchase)
                    .quantity(1)
                    .build();

            order.addOrderItem(item);
            total = total.add(priceAtPurchase);
        }

        order.setTotalPrice(total);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
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