package com.perfectmarket.modules.auth.dto;

public record UpgradeDesignerRequest(
    String specialization,
    String bio,
    String portfolioUrl,
    String skills,
    Integer experienceYears
) {}
