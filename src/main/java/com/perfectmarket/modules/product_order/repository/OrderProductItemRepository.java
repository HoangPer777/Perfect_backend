package com.perfectmarket.modules.product_order.repository;

import com.perfectmarket.modules.product_order.entity.OrderProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderProductItemRepository extends JpaRepository<OrderProductItem, Long> {
    @Query("SELECT i FROM OrderProductItem i WHERE i.order.id = :orderId")
    List<OrderProductItem> findByOrderId(@Param("orderId") UUID orderId);

    @Query("SELECT COUNT(i) > 0 FROM OrderProductItem i WHERE i.order.customerId = :customerId AND i.productId = :productId AND i.order.status = :status")
    boolean hasUserPurchasedProduct(
            @Param("customerId") UUID customerId,
            @Param("productId") UUID productId,
            @Param("status") com.perfectmarket.modules.product_order.enums.OrderStatus status
    );
}
