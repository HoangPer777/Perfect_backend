package com.perfectmarket.modules.product;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByDesignerId(UUID designerId);
    @EntityGraph(attributePaths = {"designer", "images"})
    Product findByIdAndIsActiveAndStatus(UUID id, boolean isActive, String status);

    // TODO: Add complex search queries or integrate with Meilisearch
    @Query("SELECT p FROM Product p ORDER BY p.viewCount DESC LIMIT 10")
    List<Product> findTop10ByOrderByViewCountDesc();

    @Query("SELECT p FROM Product p ORDER BY p.id DESC LIMIT 10")
    List<Product> findTop10ByOrderByIdDesc();

    @EntityGraph(attributePaths = {"designer"})
    List<Product> findByIsActiveTrueAndStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    @EntityGraph(attributePaths = {"designer"})
    List<Product> findByIsActiveTrueAndStatusOrderByViewCountDesc(String status, Pageable pageable);

    @EntityGraph(attributePaths = {"designer"})
    List<Product> findByIsActiveTrueAndStatusOrderBySoldCountDesc(String status, Pageable pageable);
}
