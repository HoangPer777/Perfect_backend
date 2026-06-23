package com.perfectmarket.modules.product;

import com.perfectmarket.modules.auth.UserPrincipal;
import com.perfectmarket.modules.product.dto.request.CreateProductRequest;
import com.perfectmarket.modules.product.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // TODO: Create Product (Designer only)
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER')")
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody CreateProductRequest createProductRequest,
                                                               Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = principal.id();
        return ResponseEntity.ok(productService.createProduct(userId, createProductRequest));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER')")
    public ResponseEntity<CreateProductResponse> updateProduct(@RequestBody CreateProductRequest createProductRequest, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = principal.id();
        return ResponseEntity.ok(productService.updateProduct(userId, createProductRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER')")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable UUID id, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = principal.id();
        return ResponseEntity.ok(productService.deleteProduct(id, userId));
    }

    // TODO: List Products with Filters (Designer, Category, Sort by price/sold)
    @GetMapping
    public String listProducts() {
        return "TODO: Implement Product Listing with Search/Filter (Meilisearch integration)";
    }

    @GetMapping("/hottest")
    public ResponseEntity<List<DesignerProjection>> getHottestDesigners(@RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(productService.getHottestDesigners(limit));
    }

    @GetMapping("/view")
    public ResponseEntity<List<CardProductResponse>> getMostViewedProducts(@RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(productService.getMostViewedProducts(limit));
    }

    @GetMapping("/newest")
    public ResponseEntity<List<CardProductResponse>> getLatestProducts(@RequestParam(required = false, defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(productService.getLatestProducts(limit));
    }

    // TODO: Get Product detail
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProductDetail(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/leaf-categories")
    public ResponseEntity<List<CategoryResponse>> getAllLeafCategories() {
        return ResponseEntity.ok(productService.findAllLeafCategories());
    }

    @GetMapping("/my-products")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DESIGNER')")
    public ResponseEntity<List<SnapshotProductResponse>> getMyProducts(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = principal.id();
        return ResponseEntity.ok(productService.getProductsByDesignerId(userId));
    }
    // TODO: Like/Comment on Product
}
