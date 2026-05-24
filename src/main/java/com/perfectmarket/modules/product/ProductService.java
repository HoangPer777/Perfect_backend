package com.perfectmarket.modules.product;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.product.dto.request.CreateProductRequest;
import com.perfectmarket.modules.product.dto.response.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public CreateProductResponse createProduct(UUID userId, CreateProductRequest request) {
        User user = userRepository.getReferenceById(userId);
        List<Category> categories = request.categories().stream()
                .map(categoryRepository::getReferenceById)
                .toList();

        Product product = Product.builder()
                .designer(user)
                .categories(categories)
                .title(request.title())
                .description(request.description())
                //.price(request.price())
                .thumbnailUrl(request.thumbnailUrl())
                .status(request.status())
                .build();

        if (request.images() != null && !request.images().isEmpty()) {
            List<ProductImage> imageEntities = request.images().stream()
                    .map(url -> ProductImage.builder().url(url).product(product).build())
                    .toList();
            product.setImages(imageEntities);
        }

        Product savedProduct = productRepository.save(product);

        return mapperProductToResponse(savedProduct);
    }

    private CreateProductResponse mapperProductToResponse(Product product) {
        List<CreateProductResponse.ProductImageResponse> images = Optional.ofNullable(product.getImages())
                .orElse(List.of())
                .stream()
                .map(p -> new CreateProductResponse.ProductImageResponse(p.getId(), p.getUrl()))
                .toList();

        List<CreateProductResponse.CategoryResponse> categories = Optional.ofNullable(product.getCategories())
                .orElse(List.of())
                .stream()
                .map(c -> new CreateProductResponse.CategoryResponse(c.getId(), c.getName()))
                .toList();
        return new CreateProductResponse(product.getId(), product.getTitle(), product.getDescription(), product.getPrice(), product.getThumbnailUrl(),
                product.getStatus(), product.getViewCount(), product.getSoldCount(), product.getRatingAvg(), images, categories,
                product.getCreatedAt(), product.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public ProductDetailResponse getProductById(UUID id) {
        Product product = productRepository.findByIdAndIsActiveAndStatus(id, true, "PUBLISHED");
        if (product == null) {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }

        ProductDetailResponse.DesignerResponse designer = new ProductDetailResponse.DesignerResponse(product.getDesigner().getId(), product.getDesigner().getEmail(),
                product.getDesigner().getUsername(), product.getDesigner().getAvatarUrl());

        List<ProductDetailResponse.ImageResponse> images = Optional.ofNullable(product.getImages()).orElse(List.of())
                .stream().map(i -> new ProductDetailResponse.ImageResponse(i.getId(), i.getUrl())).toList();

        return new ProductDetailResponse(product.getId(), designer, product.getTitle(), product.getDescription(),
                product.getPrice(), product.getThumbnailUrl(), product.getViewCount(), product.getSoldCount(),
                product.getRatingAvg(), images, product.getCreatedAt(), product.getUpdatedAt());
    }

    @Transactional(readOnly = true)
    public List<CardProductResponse> getLatestProducts(Integer limit) {
        int safeLimit = validateLimit(limit);
        Pageable pageable = PageRequest.of(0, safeLimit);

        return productRepository.findByIsActiveTrueAndStatusOrderByCreatedAtDesc("PUBLISHED", pageable)
                .stream()
                .map(this::mapToCardResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CardProductResponse> getMostViewedProducts(Integer limit) {
        int safeLimit = validateLimit(limit);
        Pageable pageable = PageRequest.of(0, safeLimit);

        return productRepository.findByIsActiveTrueAndStatusOrderByViewCountDesc("PUBLISHED", pageable)
                .stream()
                .map(this::mapToCardResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DesignerProjection> getHottestDesigners(Integer limit) {
        int safeLimit = validateLimit(limit);
        Pageable pageable = PageRequest.of(0, safeLimit);

        return productRepository.getHottestDesigner("PUBLISHED", pageable);
    }

    private int validateLimit(Integer limit) {
        if (limit == null || limit <= 0) return 10;
        return Math.min(limit, 50);
    }

    private CardProductResponse mapToCardResponse(Product p) {
        String avatarUrl = null;
        String username = null;

        if(p.getDesigner() != null) {
            avatarUrl = p.getDesigner().getAvatarUrl();
            username = p.getDesigner().getUsername();
        }

        return new CardProductResponse(
                p.getId(),
                p.getTitle(),
                p.getPrice(),
                p.getThumbnailUrl(),
                p.getRatingAvg(),
                p.getSoldCount(),
                avatarUrl,
                username
        );
    }

    public List<CategoryResponse> findAllLeafCategories() {
        return categoryRepository.findAllLeafCategories().stream().map(c -> new CategoryResponse(c.getId(), c.getName(), c.getIcon(), c.getSlug())).toList();
    }
}
