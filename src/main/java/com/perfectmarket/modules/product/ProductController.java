package com.perfectmarket.modules.product;

import com.perfectmarket.modules.product.dto.request.CreateProductRequest;
import com.perfectmarket.modules.product.dto.response.CreateProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // TODO: Create Product (Designer only)
    @PostMapping
    public ResponseEntity<CreateProductResponse.ProductResponse> createProduct(@RequestAttribute UUID userId, @RequestBody CreateProductRequest createProductRequest) {
        return ResponseEntity.ok(productService.createProduct(userId, createProductRequest));
    }

    // TODO: List Products with Filters (Designer, Category, Sort by price/sold)
    @GetMapping
    public String listProducts() {
        return "TODO: Implement Product Listing with Search/Filter (Meilisearch integration)";
    }

    // TODO: Get Product detail
    @GetMapping("/{id}")
    public String getProductDetail(@PathVariable String id) {
        return "TODO: Implement Product Detail with rating and seller info";
    }

    // TODO: Like/Comment on Product
}
