package com.perfectmarket.modules.order;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.service.ServicePackage;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "designer_id")
    private User designer;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String briefText;

    private String status; // PENDING, PROCESSING, REVIEWING, COMPLETED, DISPUTED, CANCELLED
    
    private int revisionsLeft;

    private BigDecimal actualPrice;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;

    // TODO: Add task files mapping
    // TODO: Add revisions list mapping
}
