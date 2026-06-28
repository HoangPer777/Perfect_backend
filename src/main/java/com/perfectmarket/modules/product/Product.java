package com.perfectmarket.modules.product;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.service.ServicePackage;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Column(columnDefinition = "TEXT")
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

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServicePackage> packages;

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

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setImages(List<ProductImage> newImages) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        } else {
            this.images.clear(); // Lúc này list gốc của Hibernate quản lý có thể clear nếu được khởi tạo chuẩn
        }
        if (newImages != null) {
            this.images.addAll(newImages);
        }
    }

    public void setCategories(List<Category> newCategories) {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        } else {
            this.categories.clear(); // Xóa các bản ghi cũ trong bảng trung gian thông qua proxy của Hibernate
        }

        if (newCategories != null) {
            this.categories.addAll(newCategories); // Thêm các liên kết mới vào bảng trung gian
        }
    }
}
