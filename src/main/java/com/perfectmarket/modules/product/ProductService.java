package com.perfectmarket.modules.product;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.product.dto.request.CreateProductRequest;
import com.perfectmarket.modules.product.dto.response.CreateProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public CreateProductResponse.ProductResponse createProduct(UUID userId, CreateProductRequest request) {
        Product product = new Product();
        User user = userRepository.getReferenceById(userId);
        product.setDesigner(user);
        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setThumbnailUrl(request.thumbnailUrl());
        product.setStatus(request.status());
        product = productRepository.save(product);

        if (request.images() != null && !request.images().isEmpty()) {
            Product finalProduct = product;
            List<ProductImage> imageEntities = request.images().stream()
                    .map(url -> {
                        ProductImage image = new ProductImage();
                        image.setUrl(url);
                        image.setProduct(finalProduct);
                        return image;
                    }).toList();

            List<ProductImage> savedImages = productImageRepository.saveAll(imageEntities);
            product.setImages(savedImages);
        }

        return mapperProductToResponse(product);
    }

    private CreateProductResponse.ProductResponse mapperProductToResponse(Product product) {
        List<CreateProductResponse.ProductImageResponse> images = product.getImages().stream().map(p ->
                new CreateProductResponse.ProductImageResponse(p.getId(), p.getUrl())).toList();

        return new CreateProductResponse.ProductResponse(product.getId(), product.getTitle(), product.getDescription(), product.getPrice(), product.getThumbnailUrl(),
                product.getStatus(), product.getViewCount(), product.getSoldCount(), product.getRatingAvg(), images,
                product.getCreatedAt(), product.getUpdatedAt());
    }
}
