package com.perfectmarket.modules.product.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CardProductResponse(UUID id, String title, BigDecimal price, String thumbnailUrl,
                                  Double ratingAvg, Integer soldCount, String avatarUrlDesigner, String usernameDesigner) {
}
