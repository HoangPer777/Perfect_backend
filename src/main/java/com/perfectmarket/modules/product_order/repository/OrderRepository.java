package com.perfectmarket.modules.product_order.repository;

import com.perfectmarket.modules.product_order.entity.Order;
import com.perfectmarket.modules.product_order.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository <Order, UUID>{
    List<Order> findByCustomerIdOrderByCreatedAtDesc(UUID userId);
    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    void updateStatus(@Param("orderId") UUID orderId, @Param("status") OrderStatus status);
}
