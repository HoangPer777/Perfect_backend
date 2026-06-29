package com.perfectmarket.modules.cart.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Getter @Setter @NoArgsConstructor
public class CartBanner {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId; // Liên kết với user

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartBannerItem> items = new ArrayList<>();

    public CartBanner(UUID userId) {
        this.userId = userId;
    }
}