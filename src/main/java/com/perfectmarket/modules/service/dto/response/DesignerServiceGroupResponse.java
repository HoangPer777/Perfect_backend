package com.perfectmarket.modules.service.dto.response;

import java.util.List;
import java.util.UUID;

public  record DesignerServiceGroupResponse(
        UUID designerId,
        String designerName,
        String designerAvatarUrl,
        List<ServicePackageResponse> packages
) {
}
