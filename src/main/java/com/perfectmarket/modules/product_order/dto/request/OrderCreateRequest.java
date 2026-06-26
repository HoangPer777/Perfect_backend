package com.perfectmarket.modules.product_order.dto.request;

import java.util.List;
import java.util.UUID;

public record OrderCreateRequest(
        List<UUID> productIds
) {}
