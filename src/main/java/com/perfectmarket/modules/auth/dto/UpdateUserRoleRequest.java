package com.perfectmarket.modules.auth.dto;

import java.util.UUID;

public record UpdateUserRoleRequest(UUID userId, Long roleId) {
}
