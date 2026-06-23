package com.perfectmarket.modules.order;

import com.perfectmarket.modules.order.dto.response.BarChartDto;
import com.perfectmarket.modules.order.dto.response.PieChartDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByDesignerId(UUID designerId);
    List<Task> findByCustomerId(UUID customerId);
    
    // TODO: Add filters by status (PENDING, PROCESSING, etc.)

    @Query(value = "SELECT TO_CHAR(t.completed_at, 'Dy') AS name, " +
            "       SUM(t.actual_price) AS revenue, " +
            "       COUNT(t.id) AS orders " +
            "FROM tasks t " +
            "WHERE t.status = 'COMPLETED' " +
            "  AND t.designer_id = :userId " +
            "  AND t.completed_at >= DATE_TRUNC('week', CURRENT_DATE) " +
            "GROUP BY TO_CHAR(t.completed_at, 'Dy'), EXTRACT(ISODOW FROM t.completed_at) " +
            "ORDER BY EXTRACT(ISODOW FROM t.completed_at)",
            nativeQuery = true)
    List<BarChartDto> getWeeklyBarChartData(@Param("userId") UUID userId);

    @Query(value = "SELECT p.title AS name, " +
            "       SUM(t.actual_price) AS value " +
            "FROM tasks t " +
            "JOIN service_packages sp ON t.package_id = sp.id " +
            "JOIN products p ON sp.product_id = p.id " +
            "WHERE t.status = 'COMPLETED' " +
            "  AND t.designer_id = :userId " +
            "  AND t.completed_at >= DATE_TRUNC('week', CURRENT_DATE) " +
            "GROUP BY p.id, p.title " +
            "ORDER BY value DESC",
            nativeQuery = true)
    List<PieChartDto> getWeeklyPieChartData(@Param("userId") UUID userId);

    @Query(value = "SELECT 'Week ' || CEIL(EXTRACT(DAY FROM t.completed_at) / 7.0) AS name, " +
            "       SUM(t.actual_price) AS revenue, " +
            "       COUNT(t.id) AS orders " +
            "FROM tasks t " +
            "WHERE t.status = 'COMPLETED' " +
            "  AND t.designer_id = :userId " +
            "  AND t.completed_at >= DATE_TRUNC('month', CURRENT_DATE) " +
            "GROUP BY CEIL(EXTRACT(DAY FROM t.completed_at) / 7.0) " +
            "ORDER BY CEIL(EXTRACT(DAY FROM t.completed_at) / 7.0)",
            nativeQuery = true)
    List<BarChartDto> getMonthlyBarChartData(@Param("userId") UUID userId);

    @Query(value = "SELECT p.title AS name, " +
            "       SUM(t.actual_price) AS value " +
            "FROM tasks t " +
            "JOIN service_packages sp ON t.package_id = sp.id " +
            "JOIN products p ON sp.product_id = p.id " +
            "WHERE t.status = 'COMPLETED' " +
            "  AND t.designer_id = :userId " +
            "  AND t.completed_at >= DATE_TRUNC('month', CURRENT_DATE) " +
            "GROUP BY p.id, p.title " +
            "ORDER BY value DESC",
            nativeQuery = true)
    List<PieChartDto> getMonthlyPieChartData(@Param("userId") UUID userId);

    @Query(value = "SELECT 'Q' || EXTRACT(QUARTER FROM t.completed_at) AS name, " +
            "       SUM(t.actual_price) AS revenue, " +
            "       COUNT(t.id) AS orders " +
            "FROM tasks t " +
            "WHERE t.status = 'COMPLETED' " +
            "  AND t.designer_id = :userId " +
            "  AND t.completed_at >= DATE_TRUNC('year', CURRENT_DATE) " +
            "GROUP BY EXTRACT(QUARTER FROM t.completed_at) " +
            "ORDER BY EXTRACT(QUARTER FROM t.completed_at)",
            nativeQuery = true)
    List<BarChartDto> getYearlyBarChartData(@Param("userId") UUID userId);

    @Query(value = "SELECT p.title AS name, " +
            "       SUM(t.actual_price) AS value " +
            "FROM tasks t " +
            "JOIN service_packages sp ON t.package_id = sp.id " +
            "JOIN products p ON sp.product_id = p.id " +
            "WHERE t.status = 'COMPLETED' " +
            "  AND t.designer_id = :userId " +
            "  AND t.completed_at >= DATE_TRUNC('year', CURRENT_DATE) " +
            "GROUP BY p.id, p.title " +
            "ORDER BY value DESC",
            nativeQuery = true)
    List<PieChartDto> getYearlyPieChartData(@Param("userId") UUID userId);
}
