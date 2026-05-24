package com.perfectmarket.modules.payment.port;


import com.perfectmarket.modules.payment.domain.PaymentSession;
import com.perfectmarket.modules.payment.dto.request.PaymentRequest;
import com.perfectmarket.modules.payment.dto.request.PaymentResponse;
import com.perfectmarket.modules.payment.enums.PaymentProvider;

import java.util.Map;

public interface PaymentGatewayStrategy {

    PaymentProvider getProvider();

    String createPaymentUrl(PaymentSession session);

    boolean verifySignature(Map<String, String> fields);

    PaymentResponse createPayment(PaymentRequest request);

    boolean verifyCallback(Map<String, String> queryParams);
}