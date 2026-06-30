package com.perfectmarket.modules.order;

import com.perfectmarket.modules.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.perfectmarket.modules.order.AdminTaskDetailResponse;
import com.perfectmarket.modules.order.AdminTaskListResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    // TODO: Create Task from Service Package Purchase
    @PostMapping
    public String createOrder() {
        return "TODO: Initialize Task with PENDING status and revisions_left";
    }

    // TODO: Designer: Confirm Order (PENDING -> PROCESSING)
    // TODO: Designer: Upload Draft/Watermark (PROCESSING -> REVIEWING)
    // TODO: Customer: Request Revision (REVIEWING -> REVISION_REQUESTED)
    // TODO: Customer: Confirm Final (REVIEWING -> COMPLETED)
    // TODO: Customer: Open Dispute (COMPLETED & Star < 3 -> DISPUTED)

    @PatchMapping("/{taskId}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER', 'ROLE_CUSTOMER')")
    public ResponseEntity<Task> updateStatus(@PathVariable UUID taskId, @RequestParam String status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status));
    }

    @GetMapping("/designer")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER')")
    public ResponseEntity<List<DesignerTaskResponse>> getDesignerTasks(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        UUID userId = principal.id();
        return ResponseEntity.ok(taskService.getTasksForDesigner(userId));
    }

    @GetMapping("/designer/chart")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER')")
    public ResponseEntity<Map<String, Object>> getDesignerChart(@AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.id();
        return ResponseEntity.ok(taskService.getFullDashboardAnalytics(userId));
    }

    @GetMapping("/customer")
    public ResponseEntity<java.util.List<Task>> getCustomerTasks(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(taskService.getTasksForUser(principal.id()));
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AdminTaskListResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());

    }
    @GetMapping("/admin/{taskId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<AdminTaskDetailResponse> getTaskDetail(
            @PathVariable UUID taskId) {

        return ResponseEntity.ok(taskService.getTaskDetail(taskId));
    }
}
