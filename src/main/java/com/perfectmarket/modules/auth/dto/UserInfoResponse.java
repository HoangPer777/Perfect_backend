package com.perfectmarket.modules.auth.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Builder
public record UserInfoResponse(
        UUID id,
        String email,
        String fullName,
        String username,
        String avatarUrl,
        Set<RoleResponse> roles,
        String city,
        String detailedAddress,
        String specialization,
        String bio,
        String portfolioUrl,
        String skills,
        Integer experienceYears,
        String status,
        boolean isVerified,
        Instant createdAt
) {
    @Builder
    public record RoleResponse(Long id, String name) {}
}
