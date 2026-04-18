package com.perfectmarket.modules.product;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    // TODO: Create Product (Designer only)
    @PostMapping
    public String createProduct() {
        return "TODO: Implement Product Creation";
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
