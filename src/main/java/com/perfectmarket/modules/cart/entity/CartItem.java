package com.perfectmarket.modules.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Getter @Setter @NoArgsConstructor
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Thêm dòng này
    @JoinColumn(name = "cart_id")
    private Cart cart;

    private UUID productId;
    private Integer quantity;

    public CartItem(Cart cart, UUID productId, Integer quantity) {
        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
    }
}