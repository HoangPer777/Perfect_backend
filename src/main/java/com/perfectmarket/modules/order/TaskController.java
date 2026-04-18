package com.perfectmarket.modules.order;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

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
}
