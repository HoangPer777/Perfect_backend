package com.perfectmarket.modules.service.dto.response;

import com.perfectmarket.modules.service.ServicePackage;

import java.math.BigDecimal;
import java.util.UUID;

public record ServicePackageResponse(UUID id, String title, String description, ServicePackage.PackageType packageType,
                                     BigDecimal price, int deliveryDays, int revisionsLimit) {
}
