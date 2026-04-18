package com.perfectmarket.modules.order;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findByDesignerId(UUID designerId);
    List<Task> findByCustomerId(UUID customerId);
    
    // TODO: Add filters by status (PENDING, PROCESSING, etc.)
}
