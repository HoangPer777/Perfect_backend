package com.perfectmarket.modules.product;

import com.perfectmarket.modules.product.dto.response.DesignerProjection;
import com.perfectmarket.modules.product.dto.response.SnapshotProductResponse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByDesignerId(UUID designerId);
    @EntityGraph(attributePaths = {"designer", "images"})
    Product findByIdAndIsActiveAndStatus(UUID id, boolean isActive, String status);

    @EntityGraph(attributePaths = {"designer", "images"})
    Product findByIdAndIsActiveAndDesigner_Id(UUID id, boolean isActive, UUID userId);

    @EntityGraph(attributePaths = {"designer", "images", "categories"})
    List<Product> findByDesigner_IdAndIsActiveOrderByCreatedAtDesc(UUID designerId, boolean isActive);

    @Query(value = """
    SELECT 
        p.id as id, 
        p.title as title, 
        p.thumbnailUrl as thumbnailUrl, 
        p.status as status, 
        p.viewCount as viewCount, 
        p.soldCount as soldCount, 
        p.createdAt as createdAt 
    FROM Product p 
    WHERE p.designer.id = :designerId AND p.isActive = true
    ORDER BY p.createdAt DESC
""")
    List<SnapshotProductResponse> getProductByDesignerId(@Param("designerId") UUID designerId);

    // TODO: Add complex search queries or integrate with Meilisearch
    @Query("SELECT p FROM Product p ORDER BY p.viewCount DESC LIMIT 10")
    List<Product> findTop10ByOrderByViewCountDesc();

    @Query("SELECT p FROM Product p ORDER BY p.id DESC LIMIT 10")
    List<Product> findTop10ByOrderByIdDesc();

    @EntityGraph(attributePaths = {"designer"})
    List<Product> findByIsActiveTrueAndStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    @EntityGraph(attributePaths = {"designer"})
    List<Product> findByIsActiveTrueAndStatusOrderByViewCountDesc(String status, Pageable pageable);

    @Query("""
    SELECT u.id as id, u.username as username, u.avatarUrl as avatarUrl, SUM(p.soldCount) as totalSold
    FROM Product p 
    JOIN p.designer u 
    WHERE p.isActive = true AND p.status = :status
    GROUP BY u.id, u.username, u.avatarUrl
    ORDER BY SUM(p.soldCount) DESC
    """)
    List<DesignerProjection> getHottestDesigner(String status, Pageable pageable);
}
