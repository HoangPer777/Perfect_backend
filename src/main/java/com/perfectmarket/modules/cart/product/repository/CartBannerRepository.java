package com.perfectmarket.modules.cart.product.repository;

import com.perfectmarket.modules.cart.product.entity.CartBanner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartBannerRepository extends JpaRepository<CartBanner, UUID> {
    Optional<CartBanner> findByUserId(UUID userId);
}
