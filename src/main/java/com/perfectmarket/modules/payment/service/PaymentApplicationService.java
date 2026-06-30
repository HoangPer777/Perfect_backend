package com.perfectmarket.modules.payment.service;

import com.perfectmarket.modules.cart.product.repository.CartBannerRepository;
import com.perfectmarket.modules.payment.domain.Money;
import com.perfectmarket.modules.payment.domain.PaymentSession;
import com.perfectmarket.modules.payment.domain.PaymentTransaction;
import com.perfectmarket.modules.payment.dto.response.PaymentHistoryProjection;
import com.perfectmarket.modules.payment.enums.PaymentProvider;
import com.perfectmarket.modules.payment.enums.PaymentTransactionType;
import com.perfectmarket.modules.payment.port.PaymentGatewayStrategy;
import com.perfectmarket.modules.payment.repository.PaymentSessionRepository;
import com.perfectmarket.modules.product_order.entity.Order;
import com.perfectmarket.modules.product_order.enums.OrderStatus;
import com.perfectmarket.modules.product_order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final PaymentSessionRepository sessionRepository;
    private final PaymentGatewayFactory gatewayFactory;
    private final CartBannerRepository cartRepository;
    private final OrderRepository orderRepository;
@Transactional
public String initializePayment(UUID userId, UUID orderId, Long amount, PaymentProvider provider) {
    log.info("Khởi tạo thanh toán cho User: {}, Order: {}, Amount: {}", userId, orderId, amount);

    // 1. Kiểm tra sự tồn tại của Đơn hàng để đảm bảo khóa ngoại chuẩn xác
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("Không tìm thấy Đơn hàng tương ứng với ID: " + orderId));

    // 2. Chuyển đổi số tiền an toàn (Nếu FE truyền 250, đảm bảo chuyển đổi đúng sang BigDecimal)
    BigDecimal totalAmount = (amount != null) ? BigDecimal.valueOf(amount) : order.getTotalPrice();
    if (totalAmount == null || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
        totalAmount = order.getTotalPrice(); // Dự phòng lấy từ Order trong DB nếu FE gửi sang lỗi
    }

    // 3. Khởi tạo PaymentSession
    // Lưu ý: Đảm bảo Constructor nhận đúng thứ tự tham số.
    // Nếu Constructor nhận (UUID id, UUID orderId...), hãy truyền UUID.randomUUID() lên đầu.
    PaymentSession session = new PaymentSession(
            orderId, // Hoặc UUID.randomUUID() tùy thuộc vào cấu trúc thực thể Domain của bạn
            new Money(totalAmount, "VND"),
            UUID.randomUUID().toString()
    );

    try {
        log.info("Đang tiến hành lưu Payment Session vào cơ sở dữ liệu...");
        // Sử dụng save thông thường thay vì saveAndFlush đột ngột để Hibernate tự tối ưu Transaction
        session = sessionRepository.save(session);
    } catch (Exception e) {
        log.error("LỖI KHI LƯU PAYMENT SESSION: ", e);
        throw new RuntimeException("Không thể khởi tạo phiên giao dịch thanh toán: " + e.getMessage());
    }

    session.startProcessing();
    sessionRepository.save(session);

    // 4. Gọi Gateway Strategy để sinh Link điều hướng thanh toán
    PaymentGatewayStrategy strategy = gatewayFactory.get(provider);
    String paymentUrl = strategy.createPaymentUrl(session);

    log.info("Khởi tạo đường dẫn thanh toán thành công: {}", paymentUrl);
    return paymentUrl;
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

            session.setCompleted();
            PaymentTransaction transaction = new PaymentTransaction(
                    session,
                    provider,
                    PaymentTransactionType.CHARGE,
                    session.getTotalAmount(),
                    gatewayTxnId
            );
            transaction.markSuccess(providerRef);
            session.addTransaction(transaction);

            sessionRepository.save(session);
            log.info("Thanh toán thành công. Session {} đã xử lý.", sessionId);

            // 2. Cập nhật trạng thái đơn hàng và dọn giỏ hàng
            try {
                Order order = orderRepository.findById(session.getOrderId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy Order: " + session.getOrderId()));

                // Cập nhật trực tiếp vào DB để đảm bảo dữ liệu nhất quán
                orderRepository.updateStatus(order.getId(), OrderStatus.COMPLETED);
                log.info("Đã cập nhật trạng thái đơn hàng {} thành COMPLETED trong DB", order.getId());

                List<UUID> purchasedProductIds = order.getItems().stream()
                        .map(item -> item.getProductId())
                        .toList();

                UUID customerId = order.getCustomerId();

                cartRepository.findByUserId(customerId).ifPresent(cart -> {
                    int beforeSize = cart.getItems().size();
                    boolean removed = cart.getItems().removeIf(cartItem ->
                            purchasedProductIds.contains(cartItem.getProductId())
                    );

                    if (removed) {
                        cartRepository.save(cart);
                        log.info("Đã xóa {} sản phẩm đã thanh toán khỏi giỏ hàng của user {}", (beforeSize - cart.getItems().size()), customerId);
                    }
                });
            } catch (Exception e) {
                log.error("Lỗi khi xử lý hậu thanh toán cho đơn hàng {}: {}", session.getOrderId(), e.getMessage());
            }

        } else {
            session.fail();
            sessionRepository.save(session);
            log.error("Xác thực giao dịch thất bại cho Session: {}", sessionId);
            throw new RuntimeException("Xác thực thanh toán không hợp lệ.");
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