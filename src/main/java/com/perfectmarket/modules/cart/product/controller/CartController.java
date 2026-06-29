package com.perfectmarket.modules.cart.product.controller;

import com.perfectmarket.modules.auth.security.JwtCurrentUserProvider;
import com.perfectmarket.modules.cart.product.dto.request.AddToCartRequest;
import com.perfectmarket.modules.cart.product.dto.request.UpdateCartItemRequest;
import com.perfectmarket.modules.cart.product.dto.response.CartResponse;
import com.perfectmarket.modules.cart.product.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("otherCartController")
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtCurrentUserProvider currentUserProvider;

    private UUID getUserId() {
        UUID userId = currentUserProvider.getCurrentUserId();
        if (userId == null) throw new RuntimeException("Bạn chưa đăng nhập!");
        return userId;
    }

    // 1. Thêm sản phẩm
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProductToCart(@RequestBody AddToCartRequest request) {
        cartService.addToCart(getUserId(), request);
        return ResponseEntity.ok(cartService.getCart(getUserId()));
    }

    // 2. Lấy giỏ hàng
    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart(getUserId()));
    }

    // 3. Cập nhật số lượng
    @PatchMapping("/update")
    public ResponseEntity<CartResponse> updateCartItem(@RequestBody UpdateCartItemRequest request) {
        cartService.updateItemQuantity(getUserId(), request);
        return ResponseEntity.ok(cartService.getCart(getUserId()));
    }

    // 4. Xóa một sản phẩm
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable UUID productId) {
        cartService.removeItem(getUserId(), productId);
        return ResponseEntity.ok(cartService.getCart(getUserId()));
    }

    // 5. Xóa sạch giỏ hàng
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        cartService.clearCart(getUserId());
        return ResponseEntity.ok("Giỏ hàng đã được làm trống");
    }
}