package com.perfectmarket.modules.product;

import com.perfectmarket.modules.auth.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    @ManyToMany
    @JoinTable(
            name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        viewCount = 0;
        soldCount = 0;
        ratingAvg = 0;
        isActive = true;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
