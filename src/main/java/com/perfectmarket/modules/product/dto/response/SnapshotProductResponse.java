package com.perfectmarket.modules.product.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SnapshotProductResponse {
    UUID getId();
    String getTitle();
    String getThumbnailUrl();
    String getStatus();
    int getViewCount();
    int getSoldCount();
    LocalDateTime getCreatedAt();
}
