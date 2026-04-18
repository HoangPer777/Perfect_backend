package com.perfectmarket.modules.product;

import com.perfectmarket.modules.auth.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "designer_id")
    private User designer;

    private String title;
    private String description;
    private BigDecimal price;
    private String thumbnailUrl;
    private String status; // PUBLISHED, ARCHIVED
    
    private int viewCount;
    private int soldCount;
    private double ratingAvg;

    // TODO: Add category mapping
    // TODO: Add product images list
    // TODO: Add created_at, updated_at
}
