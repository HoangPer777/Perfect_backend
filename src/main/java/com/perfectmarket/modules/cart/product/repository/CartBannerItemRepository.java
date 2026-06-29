package com.perfectmarket.modules.cart.product.repository;
import com.perfectmarket.modules.cart.product.entity.CartBannerItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CartBannerItemRepository extends JpaRepository<CartBannerItem, UUID> {
    Optional<CartBannerItem> findByCartIdAndProductId(UUID cartId, UUID productId);

    void deleteByCartIdAndProductId(UUID id, UUID productId);

    void deleteByCartId(UUID id);


}
