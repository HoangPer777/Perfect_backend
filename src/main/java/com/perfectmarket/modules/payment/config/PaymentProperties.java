package com.perfectmarket.modules.payment.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
@ConfigurationProperties(prefix = "payment")
@Getter
@Setter
public class PaymentProperties {

    private ExchangeRate exchangeRate = new ExchangeRate();
    private MomoConfig momo = new MomoConfig();
    private VnPayConfig vnpay = new VnPayConfig();
    private ZaloPayConfig zalopay = new ZaloPayConfig();
    private PayPalConfig paypal = new PayPalConfig();

    @Getter @Setter
    public static class ExchangeRate {
        private BigDecimal vndToUsd;
    }

    @Getter @Setter
    public static class MomoConfig {
        private String partnerCode;
        private String accessKey;
        private String secretKey;
        private String endpoint;
        private String callbackUrl;
        private String returnUrl;
    }

    @Getter @Setter
    public static class VnPayConfig {
        private String tmnCode;
        private String hashSecret;
        private String url;
        private String ipnUrl;
        private String returnUrl;
    }

    @Getter @Setter
    public static class ZaloPayConfig {
        private String appId;
        private String key1;
        private String key2;
        private String endpoint;
        private String callbackUrl;
        private String redirectUrl;
    }

    @Getter @Setter
    public static class PayPalConfig {
        private String clientId;
        private String clientSecret;
        private String mode;
        private String baseUrl;
        private String returnUrl;
        private String cancelUrl;
    }
}
