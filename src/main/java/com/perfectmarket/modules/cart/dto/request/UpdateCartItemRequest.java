package com.perfectmarket.modules.cart.dto.request;

import java.util.UUID;

public record UpdateCartItemRequest(UUID oldServiceId, UUID newServiceId) {
}
