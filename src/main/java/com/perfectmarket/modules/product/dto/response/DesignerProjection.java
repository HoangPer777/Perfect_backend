package com.perfectmarket.modules.product.dto.response;

import java.util.UUID;

public interface DesignerProjection {
    UUID getId();
    String getUsername();
    String getAvatarUrl();
    Long getTotalSold();
}
