package com.perfectmarket.modules.payment;

import com.perfectmarket.modules.auth.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WalletTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    private TransactionType type; // EARN, SPEND, REFUND, TOPUP

    private UUID referenceId; // ID of order or task related to this transaction
    
    private String description;
    
    private LocalDateTime createdAt;

    public enum TransactionType {
        EARN, SPEND, REFUND, TOPUP
    }

    // TODO: Add status (PENDING, SUCCESS, FAILED)
}
