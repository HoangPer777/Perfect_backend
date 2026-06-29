package com.perfectmarket.modules.product_order.repository;

import com.perfectmarket.modules.product_order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository <Order, UUID>{
    List<Order> findByCustomerIdOrderByCreatedAtDesc(UUID userId);
}
