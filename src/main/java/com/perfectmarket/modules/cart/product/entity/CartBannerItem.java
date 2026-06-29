package com.perfectmarket.modules.cart.product.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Getter @Setter @NoArgsConstructor
public class CartBannerItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Thêm dòng này
    @JoinColumn(name = "cart_id")
    private CartBanner cart;

    private UUID productId;
    private Integer quantity;

    public CartBannerItem(CartBanner cart, UUID productId, Integer quantity) {
        this.cart = cart;
        this.productId = productId;
        this.quantity = quantity;
    }
}