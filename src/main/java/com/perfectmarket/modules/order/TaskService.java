package com.perfectmarket.modules.order;

import com.perfectmarket.modules.order.dto.response.BarChartDto;
import com.perfectmarket.modules.order.dto.response.PieChartDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // TODO: Create task from order item
    @Transactional
    public Task createTask(UUID orderItemId) {
        return null;
    }

    // TODO: Designer: Accept task (PENDING -> PROCESSING)
    @Transactional
    public Task acceptTask(UUID taskId, UUID designerId) {
        return null;
    }

    // TODO: Designer: Upload draft/watermarked file (PROCESSING -> REVIEWING)
    @Transactional
    public void uploadDraft(UUID taskId, String fileUrl) {
        // TODO: Implement upload draft logic
    }

    // TODO: Customer: Request revision (REVIEWING -> REVISION_REQUESTED)
    // Reduce revisions_left by 1
    @Transactional
    public Task requestRevision(UUID taskId, String feedback) {
        return null;
    }

    // TODO: Customer: Complete task (REVIEWING -> COMPLETED)
    @Transactional
    public Task completeTask(UUID taskId, UUID customerId) {
        return null;
    }

    // TODO: Customer: Dispute task (COMPLETED & Rating < 3)
    @Transactional
    public Task disputeTask(UUID taskId, String reason) {
        return null;
    }

    public List<Task> getTasksForUser(UUID userId) {
        return taskRepository.findByCustomerId(userId);
    }

    public Map<String, Object> getFullDashboardAnalytics(UUID userId) {
        Map<String, Object> dashboardAnalytics = new HashMap<>();

        List<BarChartDto> weeklyBar = taskRepository.getWeeklyBarChartData(userId);
        List<PieChartDto> weeklyPie = taskRepository.getWeeklyPieChartData(userId);

        List<BarChartDto> monthlyBar = taskRepository.getMonthlyBarChartData(userId);
        List<PieChartDto> monthlyPie = taskRepository.getMonthlyPieChartData(userId);

        List<BarChartDto> yearlyBar = taskRepository.getYearlyBarChartData(userId);
        List<PieChartDto> yearlyPie = taskRepository.getYearlyPieChartData(userId);

        dashboardAnalytics.put("This Week", Map.of(
                "stats", calculateStats(weeklyBar),
                "bar", weeklyBar,
                "pie", weeklyPie,
                "totalPie", formatShortAmount(calculateTotalPie(weeklyPie))
        ));

        dashboardAnalytics.put("This Month", Map.of(
                "stats", calculateStats(monthlyBar),
                "bar", monthlyBar,
                "pie", monthlyPie,
                "totalPie", formatShortAmount(calculateTotalPie(monthlyPie))
        ));

        dashboardAnalytics.put("This Year", Map.of(
                "stats", calculateStats(yearlyBar),
                "bar", yearlyBar,
                "pie", yearlyPie,
                "totalPie", formatShortAmount(calculateTotalPie(yearlyPie))
        ));

        return dashboardAnalytics;
    }

    /**
     * Tính toán object "stats" từ dữ liệu Bar Chart
     */
    private Map<String, String> calculateStats(List<BarChartDto> barData) {
        if (barData == null || barData.isEmpty()) {
            return Map.of("revenue", "$0.00", "orders", "0", "revChange", "0.0%", "ordChange", "0.0%");
        }

        BigDecimal totalRevenue = barData.stream()
                .map(dto -> dto.getRevenue() != null ? dto.getRevenue() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        long totalOrders = barData.stream()
                .mapToLong(dto -> dto.getOrders() != null ? dto.getOrders() : 0L)
                .sum();

        return Map.of(
                "revenue", String.format("$%,.2f", totalRevenue),
                "orders", String.format("%,d", totalOrders),
                "revChange", "0.0%",
                "ordChange", "0.0%"
        );
    }

    /**
     * Tính tổng giá trị cho biểu đồ tròn (Pie Chart) để hiển thị ở tâm vòng tròn
     */
    private java.math.BigDecimal calculateTotalPie(List<PieChartDto> pieData) {
        if (pieData == null) return java.math.BigDecimal.ZERO;
        return pieData.stream()
                .map(dto -> dto.getValue() != null ? dto.getValue() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Định dạng rút gọn số tiền (Ví dụ: 12450.00 -> $12.4k, 624500.00 -> $624k)
     */
    private String formatShortAmount(BigDecimal amount) {
        if (amount.compareTo(java.math.BigDecimal.ZERO) == 0) return "$0k";

        double value = amount.doubleValue();
        if (value >= 1000) {
            double kilo = value / 1000.0;
            if (kilo - (int) kilo > 0) {
                return String.format("$%.1fk", kilo);
            }
            return String.format("$%dk", (int) kilo);
        }
        return String.format("$%,.0f", value);
    }

    public List<AdminTaskListResponse> getAllTasks() {

        return taskRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(task -> AdminTaskListResponse.builder()
                        .id(task.getId())
                        .customerName(task.getCustomer().getFullName())
                        .designerName(
                                task.getDesigner() != null
                                        ? task.getDesigner().getFullName()
                                        : null
                        )
                        .serviceName(task.getServicePackage().getTitle())
                        .title(task.getTitle())
                        .status(task.getStatus())
                        .actualPrice(task.getActualPrice())
                        .createdAt(task.getCreatedAt())
                        .build())
                .toList();
    }
    public AdminTaskDetailResponse getTaskDetail(UUID taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found"
                ));

        return AdminTaskDetailResponse.builder()
                .id(task.getId())
                .customerName(task.getCustomer().getFullName())
                .customerEmail(task.getCustomer().getEmail())
                .designerName(
                        task.getDesigner() != null
                                ? task.getDesigner().getFullName()
                                : null
                )
                .serviceName(task.getServicePackage().getTitle())
                .title(task.getTitle())
                .briefText(task.getBriefText())
                .status(task.getStatus())
                .actualPrice(task.getActualPrice())
                .revisionsLeft(task.getRevisionsLeft())
                .startedAt(task.getStartedAt())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .build();
    }
}