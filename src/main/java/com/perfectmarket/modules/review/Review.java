package com.perfectmarket.modules.review;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.product.Product;
import com.perfectmarket.modules.order.Task;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product; // Null if it's a task review

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task; // Null if it's a product review

    private int rating; // 1-5
    private String content;
    private String reason; // If rating < 3

    // TODO: Add images for review
    // TODO: Add created_at
}
