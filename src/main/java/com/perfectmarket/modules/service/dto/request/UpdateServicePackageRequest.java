package com.perfectmarket.modules.service.dto.request;

import com.perfectmarket.modules.service.ServicePackage;
import java.math.BigDecimal;

public record UpdateServicePackageRequest(
        String title,
        String description,
        ServicePackage.PackageType packageType,
        BigDecimal price,
        int deliveryDays,
        int revisionsLimit
) {
}
