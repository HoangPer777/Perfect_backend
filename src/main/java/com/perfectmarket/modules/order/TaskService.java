package com.perfectmarket.modules.order;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    // TODO: Create task from order item
    Task createTask(UUID orderItemId);

    // TODO: Designer: Accept task (PENDING -> PROCESSING)
    Task acceptTask(UUID taskId, UUID designerId);

    // TODO: Designer: Upload draft/watermarked file (PROCESSING -> REVIEWING)
    void uploadDraft(UUID taskId, String fileUrl);

    // TODO: Customer: Request revision (REVIEWING -> REVISION_REQUESTED)
    // Reduce revisions_left by 1
    Task requestRevision(UUID taskId, String feedback);

    // TODO: Customer: Complete task (REVIEWING -> COMPLETED)
    Task completeTask(UUID taskId, UUID customerId);

    // TODO: Customer: Dispute task (COMPLETED & Rating < 3)
    Task disputeTask(UUID taskId, String reason);

    List<Task> getTasksForUser(UUID userId);
}
