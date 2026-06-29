package com.perfectmarket.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailRequest(
    @NotBlank(message = "Token cannot be blank")
    String token,

    @Email(message = "Email must be valid")
    String email
) {}
