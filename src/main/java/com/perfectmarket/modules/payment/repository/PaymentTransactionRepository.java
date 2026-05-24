package com.perfectmarket.modules.payment.repository;

import com.perfectmarket.modules.payment.domain.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, UUID> {


    Optional<PaymentTransaction> findByProviderReference(String reference);


}
