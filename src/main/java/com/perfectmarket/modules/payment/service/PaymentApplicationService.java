package com.perfectmarket.modules.payment.service;

import com.perfectmarket.modules.cart.repository.CartRepository;
import com.perfectmarket.modules.payment.domain.Money;
import com.perfectmarket.modules.payment.domain.PaymentSession;
import com.perfectmarket.modules.payment.domain.PaymentTransaction;
import com.perfectmarket.modules.payment.dto.response.PaymentHistoryProjection;
import com.perfectmarket.modules.payment.enums.PaymentProvider;
import com.perfectmarket.modules.payment.enums.PaymentTransactionStatus;
import com.perfectmarket.modules.payment.enums.PaymentTransactionType;
import com.perfectmarket.modules.payment.port.PaymentGatewayStrategy;
import com.perfectmarket.modules.payment.repository.PaymentSessionRepository;
import com.perfectmarket.modules.product_order.entity.Order;
import com.perfectmarket.modules.product_order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final PaymentSessionRepository sessionRepository;
    private final PaymentGatewayFactory gatewayFactory;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public String initializePayment(UUID userId, UUID orderId, Long amount, PaymentProvider provider) {
        log.info("Khởi tạo thanh toán cho User: {}, Order: {}", userId, orderId);

        PaymentSession session = new PaymentSession(
                orderId,
                new Money(BigDecimal.valueOf(amount), "VND"),
                UUID.randomUUID().toString()
        );
        try {
            log.info("Đang kiểm tra tính hợp lệ của session trước khi lưu...");
            sessionRepository.saveAndFlush(session);
        } catch (Exception e) {
            log.error("LỖI KHÔNG TƯỞNG: ", e);
            throw e;
        }

        session.startProcessing();
        log.info("DEBUG: Kiểm tra giá trị trước khi save:");
        log.info(" - Session orderId: {}", session.getOrderId());
        log.info(" - Session Amount: {}", session.getTotalAmount()); // Kiểm tra xem Money có bị null không
        log.info(" - Session IdempotencyKey: {}", session.getIdempotencyKey());
        sessionRepository.save(session);

        PaymentGatewayStrategy strategy = gatewayFactory.get(provider);
        return strategy.createPaymentUrl(session);
    }

    @Transactional
    public void processPaymentResult(Map<String, String> params, PaymentProvider provider) {
        UUID sessionId = extractSessionId(params, provider);
        log.info("Xử lý kết quả thanh toán cho {} - Session ID: {}", provider, sessionId);

        PaymentSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Session: " + sessionId));

        PaymentGatewayStrategy strategy = gatewayFactory.get(provider);
        boolean isValid = strategy.verifyCallback(params);

        if (isValid) {
            String gatewayTxnId = provider == PaymentProvider.VNPAY ? params.get("vnp_TransactionNo") : params.get("token");
            String providerRef = provider == PaymentProvider.VNPAY ? params.get("vnp_BankCode") : params.get("PayerID");

            PaymentTransaction transaction = new PaymentTransaction(
                    session,
                    provider,
                    PaymentTransactionType.CHARGE,
                    session.getTotalAmount(),
                    gatewayTxnId
            );
            transaction.markSuccess(providerRef);

            session.addTransaction(transaction);
            log.info("Thanh toán thành công. Session {} đã xử lý.", sessionId);

            try {
                // 1. Truy vấn Order để lấy đúng Customer ID
                Order order = orderRepository.findById(session.getOrderId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Order: " + session.getOrderId()));

                UUID customerId = order.getCustomerId();

                // 2. Dọn dẹp giỏ hàng
                cartRepository.findByUserId(customerId).ifPresent(cart -> {
                    // Việc gọi clear() và save() sẽ kích hoạt xóa SQL nếu có orphanRemoval = true
                    cart.getItems().clear();
                    cartRepository.save(cart);
                    log.info("Giỏ hàng của user {} đã được làm sạch sau thanh toán.", customerId);
                });
            } catch (Exception e) {
                log.error("Lỗi khi dọn dẹp giỏ hàng sau thanh toán cho đơn hàng {}: {}", session.getOrderId(), e.getMessage());
            }

        } else {
            session.fail();
            log.error("Xác thực giao dịch thất bại cho Session: {}", sessionId);
        }
    }

    private UUID extractSessionId(Map<String, String> params, PaymentProvider provider) {
        try {
            String rawId = switch (provider) {
                case MOMO -> params.get("orderId");
                case VNPAY -> params.get("vnp_TxnRef");
                case PAYPAL -> params.get("sessionId");
                case ZALOPAY -> {
                    String appTransId = params.get("app_trans_id");
                    yield appTransId.contains("_") ? appTransId.substring(appTransId.indexOf("_") + 1) : appTransId;
                }
                default -> throw new RuntimeException("Provider not supported");
            };

            return UUID.fromString(rawId);
        } catch (Exception e) {
            log.error("Lỗi trích xuất Session ID từ {}: {}", provider, e.getMessage());
            throw new RuntimeException("Lỗi trích xuất Session ID từ " + provider + ": ID không đúng định dạng UUID");
        }
    }

    @Transactional(readOnly = true)
    public List<PaymentHistoryProjection> getPaymentsByUserId(UUID userId) {
        log.info(">>>> [SERVICE] Đang tải lịch sử giao dịch cho User ID: {}", userId);
        return sessionRepository.findPaymentHistoryByUserId(userId);
    }
}