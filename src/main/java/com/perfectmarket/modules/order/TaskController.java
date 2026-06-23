package com.perfectmarket.modules.order;

import com.perfectmarket.modules.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public String updateStatus(@PathVariable String taskId, @RequestParam String status) {
        return "TODO: Implement Task State Machine Transitions";
    }

    // TODO: Download brief/documents (S3 integration)

    @GetMapping("/designer/chart")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER')")
    public ResponseEntity<Map<String, Object>> getDesignerChart(@AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.id();
        return ResponseEntity.ok(taskService.getFullDashboardAnalytics(userId));
    }
}
