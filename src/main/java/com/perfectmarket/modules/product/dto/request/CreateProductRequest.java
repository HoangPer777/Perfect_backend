package com.perfectmarket.modules.product.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateProductRequest(UUID userId, String title, String description, BigDecimal price,
                                   String thumbnailUrl, String status, List<String> images, List<UUID> categories) { }
