package com.perfectmarket.modules.auth.dto;

import com.perfectmarket.modules.auth.Role;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public interface SnapshotUserResponse {
    UUID getId();
    String getEmail();
    String getUsername();
    String getAvatarUrl();
    String getStatus();
    Set<Role> getRoles();
    Instant getCreatedAt();
}
