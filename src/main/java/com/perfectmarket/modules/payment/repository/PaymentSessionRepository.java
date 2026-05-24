package com.perfectmarket.modules.payment.repository;

import com.perfectmarket.modules.payment.domain.PaymentSession;
import com.perfectmarket.modules.payment.dto.response.PaymentHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentSessionRepository extends JpaRepository<PaymentSession, UUID> {

    Optional<PaymentSession> findByOrderId(UUID orderId);

    Optional<PaymentSession> findByIdempotencyKey(String idempotencyKey);

    @Query(value = "SELECT s.id AS sessionId, " +
                   "       s.order_id AS orderId, " +
                   "       s.total_amount_amount AS amount, " +
                   "       t.provider AS provider, " +
                   "       s.status AS status, " +
                   "       s.created_at AS createdAt, " +
                   "       t.gateway_txn_id AS transactionId, " +
                   "       t.created_at AS paymentDate " +
                   "FROM payment_sessions s " +
                   "JOIN orders o ON s.order_id = o.id " +
                   "LEFT JOIN payment_transactions t ON t.session_id = s.id " +
                   "WHERE o.user_id = :userId " +
                   "ORDER BY s.created_at DESC", nativeQuery = true)
    List<PaymentHistoryProjection> findPaymentHistoryByUserId(@Param("userId") UUID userId);
}