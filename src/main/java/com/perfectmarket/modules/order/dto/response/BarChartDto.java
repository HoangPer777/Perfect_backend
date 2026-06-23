package com.perfectmarket.modules.order.dto.response;

import java.math.BigDecimal;

public interface BarChartDto {
    String getName();       // Tên cột (Mon, Tue / Week 1, Week 2 / Q1, Q2...)
    BigDecimal getRevenue(); // Doanh thu tổng
    Long getOrders();       // Tổng số đơn hàng
}