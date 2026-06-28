package com.perfectmarket.modules.cart;

import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.cart.dto.response.AddCartItemResponse;
import com.perfectmarket.modules.cart.dto.response.CartItemResponse;
import com.perfectmarket.modules.product.Product;
import com.perfectmarket.modules.service.ServicePackage;
import com.perfectmarket.modules.service.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ServicePackageRepository servicePackageRepository;

    public Page<CartItemResponse> getCartItems(UUID userId, Pageable pageable) {
        return cartRepository.findAllByUser_Id(userId, pageable).map(this::mapperToCartItemResponse);
    }

    public Integer countCartItems(UUID userId) {
        return cartRepository.countByUser_Id(userId);
    }

    private CartItemResponse mapperToCartItemResponse(CartItem cartItem) {
        Product product = cartItem.getServicePackage().getProduct();
        ServicePackage servicePackage = cartItem.getServicePackage();
        CartItemResponse response = CartItemResponse.builder()
                .id(cartItem.getId())
                .product(CartItemResponse.ProductResponse.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .thumbnailUrl(product.getThumbnailUrl())
                        .designerId(product.getDesigner().getId())
                        .designerUsername(product.getDesigner().getUsername())
                        .build())
                .serviceId(servicePackage.getId())
                .title(servicePackage.getTitle())
                .price(servicePackage.getPrice())
                .deliveryDays(servicePackage.getDeliveryDays())
                .packageType(servicePackage.getPackageType())
                .revisionsLimit(servicePackage.getRevisionsLimit())
                .build();
        return response;
    }

    public AddCartItemResponse addCartItem(UUID userId, UUID serviceId) {
        if(cartRepository.existsByUser_IdAndServicePackage_Id(userId, serviceId)) {
            return new AddCartItemResponse(false, true);
        }
        CartItem cartItem = CartItem.builder()
                .user(userRepository.getReferenceById(userId))
                .servicePackage(servicePackageRepository.findById(serviceId).orElseThrow(
                        () -> new NoSuchElementException("Not existing service package!")
                ))
                .build();
        cartItem = cartRepository.save(cartItem);
        return new AddCartItemResponse(cartItem.getId() != null, cartItem.getId() != null);
    }

    @Transactional
    public boolean deleteCartItem(UUID userId, UUID cartItemId) {
        int rowChanges = cartRepository.deleteByIdAndUser_Id(cartItemId, userId);
        return rowChanges > 0;
    }

}
