package com.perfectmarket.modules.cart.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
@Getter @Setter @NoArgsConstructor
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId; // Liên kết với user

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public Cart(UUID userId) {
        this.userId = userId;
    }
}