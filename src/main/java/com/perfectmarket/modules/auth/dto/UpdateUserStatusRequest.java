package com.perfectmarket.modules.auth.dto;

import java.util.UUID;

public record UpdateUserStatusRequest(UUID userId, String status) {
}
