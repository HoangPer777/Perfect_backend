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
public class AdminTaskListResponse {

    private UUID id;
    private String customerName;
    private String designerName;
    private String serviceName;
    private String title;
    private String status;
    private BigDecimal actualPrice;
    private LocalDateTime createdAt;

}
