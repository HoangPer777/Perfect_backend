package com.perfectmarket.modules.order;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesignerTaskResponse {
    private UUID id;
    private CustomerInfo customer;
    private ServicePackageInfo servicePackage;
    private String title;
    private String briefText;
    private String status;
    private int revisionsLeft;
    private BigDecimal actualPrice;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerInfo {
        private UUID id;
        private String fullName;
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicePackageInfo {
        private UUID id;
        private String title;
        private BigDecimal price;
    }
}
