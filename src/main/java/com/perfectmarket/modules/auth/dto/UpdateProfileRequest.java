package com.perfectmarket.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @NotBlank(message = "Full name cannot be blank")
    String fullName,

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    String avatarUrl,
    String city,
    String detailedAddress,
    boolean emailNotifications,
    boolean promotionalOffers
) {}
