package com.perfectmarket.modules.payment.domain;

import com.perfectmarket.modules.payment.enums.PaymentProvider;
import com.perfectmarket.modules.payment.enums.StoredPaymentMethodStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stored_payment_methods",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_provider_external_token", columnNames = {"provider", "external_token"})
        })
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoredPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentProvider provider;

    @Column(name = "external_token", nullable = false)
    private String externalToken;

    @Column(nullable = false)
    private boolean isDefault;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StoredPaymentMethodStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public StoredPaymentMethod(UUID userId, PaymentProvider provider, String externalToken) {
        if (externalToken == null || externalToken.isBlank()) {
            throw new RuntimeException("Invalid payment provider token");
        }
        this.userId = userId;
        this.provider = provider;
        this.externalToken = externalToken;
        this.status = StoredPaymentMethodStatus.ACTIVE;
        this.isDefault = false;
        this.createdAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = StoredPaymentMethodStatus.INACTIVE;

    }

    public void unsetDefault() {
        this.isDefault = false;
    }

    public void setDefault() {
        this.isDefault = true;

    }
}