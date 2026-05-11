package com.perfectmarket.modules.product;

import com.perfectmarket.modules.product.dto.request.CreateProductRequest;
import com.perfectmarket.modules.product.dto.response.CardProductResponse;
import com.perfectmarket.modules.product.dto.response.CreateProductResponse;
import com.perfectmarket.modules.product.dto.response.DesignerProjection;
import com.perfectmarket.modules.product.dto.response.ProductDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // TODO: Create Product (Designer only)
    // FIXME: Get userId and check role from Spring Security (after complete login)
    @PostMapping("/add")
    public ResponseEntity<CreateProductResponse> createProduct(@RequestAttribute UUID userId, @RequestBody CreateProductRequest createProductRequest) {
        return ResponseEntity.ok(productService.createProduct(userId, createProductRequest));
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

    // TODO: Like/Comment on Product
}
