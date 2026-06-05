package com.perfectmarket.modules.cart;

import com.perfectmarket.modules.auth.UserPrincipal;
import com.perfectmarket.modules.cart.dto.request.UpdateCartItemRequest;
import com.perfectmarket.modules.cart.dto.response.CartItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/get")
    public ResponseEntity<Page<CartItemResponse>> getCarts(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal UserPrincipal principal) {

        UUID userId = principal.id();
        return ResponseEntity.ok(cartService.getCartItems(userId, pageable));
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addCartItem(@RequestBody UUID serviceId, @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.id();
        return ResponseEntity.ok(cartService.addCartItem(userId, serviceId));
    }

    @PutMapping("/update")
    public ResponseEntity<CartItemResponse> updateCartItem(@RequestBody UpdateCartItemRequest request,
                                                           @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.id();
        return ResponseEntity.ok(cartService.changeServicePackage(userId, request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteCartItem(@PathVariable UUID id, @AuthenticationPrincipal UserPrincipal principal) {
        UUID userId = principal.id();
        return ResponseEntity.ok(cartService.deleteCartItem(userId, id));
    }

}
