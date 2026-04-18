package com.perfectmarket.modules.payment;

import com.perfectmarket.modules.auth.User;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Wallet {
    @Id
    private UUID userId;

    @OneToOne
    @MapsId
    private User user;

    private BigDecimal balance;

    // TODO: Add concurrency locking for balance updates
    // TODO: Add created_at, updated_at
}
