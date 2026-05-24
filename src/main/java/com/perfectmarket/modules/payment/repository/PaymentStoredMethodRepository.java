package com.perfectmarket.modules.payment.repository;

import com.perfectmarket.modules.payment.domain.StoredPaymentMethod;
import com.perfectmarket.modules.payment.enums.PaymentProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface PaymentStoredMethodRepository extends JpaRepository<StoredPaymentMethod, UUID> {

    boolean existsByUserId(UUID userId);

    Optional<StoredPaymentMethod> findByUserIdAndIsDefaultTrue(UUID userId);

    Optional<StoredPaymentMethod> findByProviderAndExternalToken(PaymentProvider provider, String token);
}
