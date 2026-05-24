package com.perfectmarket.modules.payment.domain;

import com.perfectmarket.modules.payment.enums.PaymentSessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "payment_sessions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @Embedded
    private Money totalAmount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private List<PaymentTransaction> transactions = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentSessionStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    public PaymentSession(UUID orderId, Money totalAmount, String idempotencyKey) {
        if (orderId == null || totalAmount == null || idempotencyKey == null) {
            throw new RuntimeException("Invalid payment session params");
        }
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.idempotencyKey = idempotencyKey;
        this.status = PaymentSessionStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public long getAmount() {
        return this.totalAmount.getAmount().longValue() ;
    }

    public void startProcessing() {
        this.status = PaymentSessionStatus.PROCESSING;
    }

    public void fail() {
        this.status = PaymentSessionStatus.FAILED;

    }

    public void addTransaction(PaymentTransaction transaction) {
        this.transactions.add(transaction);

    }
}