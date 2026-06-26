package com.perfectmarket.modules.payment.controller;

import com.perfectmarket.modules.auth.security.CurrentUserProvider;
import com.perfectmarket.modules.payment.dto.command.AddPaymentMethodCommand;
import com.perfectmarket.modules.payment.dto.request.PaymentInitRequest;
import com.perfectmarket.modules.payment.dto.response.PaymentHistoryProjection;
import com.perfectmarket.modules.payment.enums.PaymentProvider;
import com.perfectmarket.modules.payment.service.PaymentApplicationService;
import com.perfectmarket.modules.payment.service.PaymentMethodManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentMethodManagementService paymentMethodManagementService;
    private final PaymentApplicationService paymentService; // Dùng 1 biến duy nhất cho service thanh toán
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/methods")
    public ResponseEntity<Void> addPaymentMethod(@RequestBody @Valid AddPaymentMethodCommand command) {
        paymentMethodManagementService.addStoredPaymentMethod(currentUserProvider.getCurrentUserId(), command);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/methods/{paymentMethodId}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable UUID paymentMethodId) {
        paymentMethodManagementService.deleteStoredPaymentMethod(currentUserProvider.getCurrentUserId(), paymentMethodId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/init")
    public ResponseEntity<Map<String, String>> initializePayment(@RequestBody @Valid PaymentInitRequest request) {
        UUID userId = currentUserProvider.getCurrentUserId();
        log.info("Khởi tạo thanh toán cho User: {} - Order: {}", userId, request.orderId());
        var auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("DEBUG: Auth trong Controller: " + (auth != null ? auth.getPrincipal() : "NULL"));
        String paymentUrl = paymentService.initializePayment(
                userId,
                request.orderId(),
                request.amount(),
                request.provider()
        );
        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }


    @GetMapping(value = "/callback/{provider}", produces = org.springframework.http.MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> handlePaymentReturn(@PathVariable String provider, @RequestParam Map<String, String> allParams) {
        try {
            PaymentProvider paymentProvider = PaymentProvider.valueOf(provider.toUpperCase());
            paymentService.processPaymentResult(allParams, paymentProvider);
            return ResponseEntity.ok(getSuccessHtml(provider.toUpperCase()));
        } catch (Exception e) {
            log.error("Lỗi xử lý callback {}: {}", provider, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<html><body><h1>Lỗi xử lý giao dịch!</h1></body></html>");
        }
    }

    @GetMapping("/webhook/vnpay")
    public ResponseEntity<Map<String, String>> handleVNPayIPN(@RequestParam Map<String, String> allParams) {
        try {
            paymentService.processPaymentResult(allParams, PaymentProvider.VNPAY);
            return ResponseEntity.ok(Map.of("RspCode", "00", "Message", "Confirm Success"));
        } catch (Exception e) {
            log.error("VNPAY IPN error: ", e);
            return ResponseEntity.ok(Map.of("RspCode", "99", "Message", "Unknown error"));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentHistoryProjection>> getPaymentHistory(@PathVariable UUID userId) {
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }

    private String getSuccessHtml(String provider) {
        return "<html><body style='text-align:center; font-family:sans-serif; padding-top: 50px;'>" +
               "<h1 style='color: #1e3a8a; font-weight: bold;'>Thanh toán " + provider + " thành công!</h1>" +
               "<p style='font-size: 18px; color: #4b5563;'>Vui lòng đóng cửa sổ này và tiếp tục mua sắm.</p>" +
               "<button onclick='window.close()' style='margin-top: 20px; padding: 12px 24px; background: #1e3a8a; color: white; border: none; border-radius: 8px; cursor: pointer; font-weight: bold;'>Đóng cửa sổ</button>" +
               "</body></html>";
    }
}