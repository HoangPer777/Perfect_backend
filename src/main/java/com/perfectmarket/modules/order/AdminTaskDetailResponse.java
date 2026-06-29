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
public class AdminTaskDetailResponse {
    private UUID id;

    private String customerName;
    private String customerEmail;
    private String customerPhone;

    private String designerName;

    private String serviceName;

    private String title;
    private String briefText;

    private String status;

    private BigDecimal actualPrice;

    private Integer revisionsLeft;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
}