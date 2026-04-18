package com.perfectmarket.modules.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByDesignerId(UUID designerId);
    
    // TODO: Add complex search queries or integrate with Meilisearch
    @Query("SELECT p FROM Product p ORDER BY p.viewCount DESC LIMIT 10")
    List<Product> findTop10ByOrderByViewCountDesc();

    @Query("SELECT p FROM Product p ORDER BY p.id DESC LIMIT 10")
    List<Product> findTop10ByOrderByIdDesc();
}
