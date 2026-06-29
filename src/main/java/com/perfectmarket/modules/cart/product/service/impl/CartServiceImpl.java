package com.perfectmarket.modules.cart.product.service.impl;

import com.perfectmarket.modules.cart.product.dto.request.AddToCartRequest;
import com.perfectmarket.modules.cart.product.dto.request.UpdateCartItemRequest;
import com.perfectmarket.modules.cart.product.dto.response.CartResponse;
import com.perfectmarket.modules.cart.product.entity.CartBanner;
import com.perfectmarket.modules.cart.product.entity.CartBannerItem;
import com.perfectmarket.modules.cart.product.repository.CartBannerItemRepository;
import com.perfectmarket.modules.cart.product.repository.CartBannerRepository;
import com.perfectmarket.modules.cart.product.service.CartService;
import com.perfectmarket.modules.product.Product;
import com.perfectmarket.modules.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartBannerRepository cartRepository;
    @Autowired private CartBannerItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Transactional // Đảm bảo toàn bộ thao tác là một transaction
    public void addToCart(UUID userId, AddToCartRequest request) {
        if (userId == null) {
            throw new RuntimeException("Người dùng chưa đăng nhập!");
        }

        CartBanner cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartBanner newCart = new CartBanner(userId);
                    return cartRepository.save(newCart);
                });

        Optional<CartBannerItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId());

        if (existingItem.isPresent()) {
            CartBannerItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
            cartItemRepository.save(item);
        } else {
            CartBannerItem newItem = new CartBannerItem(cart, request.productId(), request.quantity());
            cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public void removeItem(UUID userId, UUID productId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);
        });
    }

    @Transactional
    public void clearCart(UUID userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cartItemRepository.deleteByCartId(cart.getId());
        });
    }

    public CartResponse getCart(UUID userId) {
        CartBanner cart = cartRepository.findByUserId(userId)
                .orElse(new CartBanner(userId));
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            return new CartResponse(cart.getId(), List.of(), BigDecimal.ZERO);
        }
        return convertToResponse(cart);
    }

    @Override
    @Transactional
    public void updateItemQuantity(UUID userId, UpdateCartItemRequest request) {
        CartBanner cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));
        CartBannerItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.productId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ hàng"));
        if (request.quantity() > 0) {
            item.setQuantity(request.quantity());
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }
    }

    private CartResponse convertToResponse(CartBanner cart) {
        List<UUID> productIds = cart.getItems().stream()
                .map(CartBannerItem::getProductId).toList();

        List<Product> products = productRepository.findAllById(productIds);
        Map<UUID, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<CartResponse.CartItemResponse> items = cart.getItems().stream()
                .map(item -> {
                    Product p = productMap.get(item.getProductId());
                    return new CartResponse.CartItemResponse(
                            item.getId(),
                            item.getProductId(),
                            p != null ? p.getTitle() : "Unknown Product",
                            p != null ? p.getThumbnailUrl() : null,
                            p != null ? p.getPrice() : BigDecimal.ZERO,
                            item.getQuantity()
                    );
                }).toList();

        BigDecimal total = items.stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(cart.getId(), items, total);
    }
}