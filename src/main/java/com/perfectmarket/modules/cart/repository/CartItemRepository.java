package com.perfectmarket.modules.cart.repository;

import com.perfectmarket.modules.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    Optional<CartItem> findByCartIdAndProductId(UUID cartId, UUID productId);

    void deleteByCartIdAndProductId(UUID id, UUID productId);

    void deleteByCartId(UUID id);
}
