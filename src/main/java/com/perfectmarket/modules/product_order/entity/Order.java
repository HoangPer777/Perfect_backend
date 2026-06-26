package com.perfectmarket.modules.product_order.entity;

import com.perfectmarket.modules.product_order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp; // Import này
import java.math.BigDecimal;
import java.time.LocalDateTime; // Import này
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID customerId;
    private UUID designerId;
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProductItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // Thêm trường createdAt
    @CreationTimestamp
    @Column(updatable = true)
    private LocalDateTime createdAt;

    public void addOrderItem(OrderProductItem item) {
        items.add(item);
        item.setOrder(this);
    }
}