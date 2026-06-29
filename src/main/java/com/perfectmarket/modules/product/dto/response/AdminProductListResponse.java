package com.perfectmarket.modules.product.dto.response;

import com.perfectmarket.modules.service.dto.response.ServicePackageResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AdminProductListResponse(
        UUID id,
        String title,
        String thumbnailUrl,
        String designerName,
        String status,
        BigDecimal price,
        ServicePackageResponse basic,
        ServicePackageResponse pro,
        ServicePackageResponse vip,
        LocalDateTime createdAt
) {
}