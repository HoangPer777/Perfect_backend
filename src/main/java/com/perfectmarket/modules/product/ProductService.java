package com.perfectmarket.modules.product;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.product.dto.request.CreateProductRequest;
import com.perfectmarket.modules.product.dto.response.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transactional
    public CreateProductResponse updateProduct(UUID userId, CreateProductRequest request) {
        Product product = productRepository.findByIdAndIsActiveAndDesigner_Id(request.id(), true, userId);
        if(product == null) {
            throw new EntityNotFoundException("Product not found");
        }

        product.setTitle(request.title());
        product.setDescription(request.description());
        product.setThumbnailUrl(request.thumbnailUrl());
        product.setStatus(request.status());

        List<ProductImage> currentImages = product.getImages();
        if (currentImages == null) {
            currentImages = new ArrayList<>();
        }

        Map<String, ProductImage> existingImagesMap = currentImages.stream()
                .collect(Collectors.toMap(ProductImage::getUrl, image -> image, (existing, replacement) -> existing));

        List<ProductImage> updatedImages = new ArrayList<>();

        for (String url : request.images()) {
            if (existingImagesMap.containsKey(url)) {
                updatedImages.add(existingImagesMap.get(url));
            } else {
                ProductImage newImage = new ProductImage();
                newImage.setUrl(url);
                newImage.setProduct(product);
                updatedImages.add(newImage);
            }
        }

        product.setImages(updatedImages);

        List<Category> categories = request.categories().stream()
                .map(categoryRepository::getReferenceById)
                .collect(Collectors.toList());
        product.setCategories(categories);

        Product savedProduct = productRepository.save(product);
        return mapperProductToResponse(savedProduct);
    }

    public Boolean deleteProduct(UUID productId, UUID userId) {
        Product product = productRepository.findByIdAndIsActiveAndDesigner_Id(productId, true, userId);
        if (product == null) {
            throw new EntityNotFoundException("Product not found");
        }

        product.setActive(false);
        productRepository.save(product);
        return true;
    }

    public CreateProductResponse getProductByDesigner(UUID userId, UUID productId) {
        Product product = productRepository.findByIdAndIsActiveAndDesigner_Id(productId, true, userId);
        if(product == null) {
            throw new EntityNotFoundException("Product not found");
        }

        return mapperProductToResponse(product);
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

        List<ProductDetailResponse.CategoryResponse> categories = Optional.ofNullable(product.getCategories())
                .orElse(List.of())
                .stream()
                .map(c -> new ProductDetailResponse.CategoryResponse(c.getId(), c.getName()))
                .toList();


        return new ProductDetailResponse(product.getId(), designer, product.getTitle(), product.getDescription(),
                product.getPrice(), product.getThumbnailUrl(), product.getViewCount(), product.getSoldCount(),
                product.getRatingAvg(), images, categories, product.getCreatedAt(), product.getUpdatedAt());
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

    @Transactional(readOnly = true)
    public List<SnapshotProductResponse> getProductsByDesignerId(UUID designerId) {
        return productRepository.getProductByDesignerId(designerId);
    }

    public List<CategoryResponse> findAllRootCategories() {
        return categoryRepository.findAllRootCategories().stream().map(c -> new CategoryResponse(c.getId(), c.getName(), c.getIcon(), c.getSlug())).toList();
    }

    @Transactional(readOnly = true)
    public Page<CardProductResponse> getFilteredProducts(String categoryIdStr, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, Pageable pageable) {

        List<UUID> categoryIds = null;

        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty() && !categoryIdStr.equalsIgnoreCase("All")) {
            try {
                UUID categoryId = UUID.fromString(categoryIdStr.trim());
                categoryIds = new ArrayList<>(categoryRepository.findImmediateChildIds(categoryId));
                categoryIds.add(categoryId);
            } catch (Exception e) {
                throw new EntityNotFoundException();
            }
        }

        Page<Product> result;
        String sortKey = (sortBy != null) ? sortBy.toLowerCase().trim() : "";

        switch (sortKey) {
            case "price-asc":
                result = productRepository.searchProductsOrderByPriceAsc(categoryIds, minPrice, maxPrice, pageable);
                break;
            case "price-desc":
                result = productRepository.searchProductsOrderByPriceDesc(categoryIds, minPrice, maxPrice, pageable);
                break;
            case "best-seller":
                result = productRepository.searchProductsOrderBySoldCountDesc(categoryIds, minPrice, maxPrice, pageable);
                break;
            default:
                result = productRepository.searchProductsDefault(categoryIds, minPrice, maxPrice, pageable);
                break;
        }

        return result.map(this::mapToCardResponse);
    }

}
