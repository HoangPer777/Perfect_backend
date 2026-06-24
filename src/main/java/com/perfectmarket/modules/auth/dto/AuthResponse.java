package com.perfectmarket.modules.auth.dto;

import java.util.Set;
import java.util.UUID;

public record AuthResponse(
    String accessToken,
    String tokenType,
    UserInfo user
) {
    public record UserInfo(
        UUID id,
        String email,
        String fullName,
        String username,
        String avatarUrl,
        Set<String> roles,
        String city,
        String detailedAddress,
        boolean emailNotifications,
        boolean promotionalOffers
    ) {}

    public static AuthResponse of(String token, com.perfectmarket.modules.auth.User user) {
        var roles = user.getRoles().stream()
            .map(r -> r.getName())
            .collect(java.util.stream.Collectors.toSet());
        return new AuthResponse(
            token,
            "Bearer",
            new UserInfo(
                user.getId(), user.getEmail(), user.getFullName(),
                user.getUsername(), user.getAvatarUrl(), roles,
                user.getCity(), user.getDetailedAddress(),
                user.isEmailNotifications(), user.isPromotionalOffers()
            )
        );
    }
}
