package com.perfectmarket.modules.product.dto.response;

import java.util.UUID;

public record CategoryResponse(UUID id, String name, String iconUrl, String slug) {
}
