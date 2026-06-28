package com.perfectmarket.modules.cart.dto.response;

import com.perfectmarket.modules.service.ServicePackage;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record CartItemResponse(UUID id, ProductResponse product, UUID serviceId, String title,
                               ServicePackage.PackageType packageType,
                               BigDecimal price, int deliveryDays, int revisionsLimit) {
    @Builder
    public record ProductResponse(UUID id, UUID designerId, String designerUsername, String title, String thumbnailUrl) {}
}
