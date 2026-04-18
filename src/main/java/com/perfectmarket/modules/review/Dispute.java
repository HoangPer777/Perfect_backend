package com.perfectmarket.modules.review;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.order.Task;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "disputes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Dispute {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "designer_id")
    private User designer;

    private String reason;
    private String status; // OPEN, RESOLVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "resolved_by")
    private User admin;

    private LocalDateTime createdAt;
    private LocalDateTime resolvedAt;

    // TODO: Add dispute comments/messages thread
}
