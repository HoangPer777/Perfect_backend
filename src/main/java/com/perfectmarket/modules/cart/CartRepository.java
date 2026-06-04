package com.perfectmarket.modules.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<CartItem, UUID> {
    @EntityGraph(attributePaths = {"servicePackage", "servicePackage.product", "servicePackage.product.designer"})
    Page<CartItem> findAllByUser_Id(UUID id, Pageable pageable);
    CartItem findByUser_IdAndServicePackage_Id(UUID id, UUID servicePackageId);
}
