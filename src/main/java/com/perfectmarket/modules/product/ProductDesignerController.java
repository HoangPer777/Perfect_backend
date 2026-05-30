package com.perfectmarket.modules.product;

import com.perfectmarket.modules.auth.UserPrincipal;
import com.perfectmarket.modules.product.dto.response.CreateProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/designer/products")
@RequiredArgsConstructor
public class ProductDesignerController {
    private final ProductService productService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_DESIGNER')")
    public ResponseEntity<CreateProductResponse> getProductByDesigner(@PathVariable UUID id, Authentication authentication) {
        System.out.println(id + " " + authentication.getName());
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        UUID userId = principal.id();
        return ResponseEntity.ok(productService.getProductByDesigner(userId, id));
    }
}
