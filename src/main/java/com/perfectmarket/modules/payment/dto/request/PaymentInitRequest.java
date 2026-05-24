package com.perfectmarket.modules.payment.dto.request;


import com.perfectmarket.modules.payment.enums.PaymentProvider;

import java.util.UUID;

public record PaymentInitRequest(
        UUID orderId,
        Long amount,
        PaymentProvider provider
) {}