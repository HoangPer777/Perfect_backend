package com.perfectmarket.modules.cart;

import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.cart.dto.request.UpdateCartItemRequest;
import com.perfectmarket.modules.cart.dto.response.CartItemResponse;
import com.perfectmarket.modules.product.Product;
import com.perfectmarket.modules.service.ServicePackage;
import com.perfectmarket.modules.service.ServicePackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    private CartItemResponse mapperToCartItemResponse(CartItem cartItem) {
        Product product = cartItem.getServicePackage().getProduct();
        ServicePackage servicePackage = cartItem.getServicePackage();
        CartItemResponse response = CartItemResponse.builder()
                .id(cartItem.getId())
                .product(CartItemResponse.ProductResponse.builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .thumbnailUrl(product.getThumbnailUrl())
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

    public CartItemResponse addCartItem(UUID userId, UUID serviceId) {
        CartItem cartItem = CartItem.builder()
                .user(userRepository.getReferenceById(userId))
                .servicePackage(servicePackageRepository.findById(serviceId).orElseThrow(
                        () -> new NoSuchElementException("Not existing service package!")
                ))
                .build();
        cartItem = cartRepository.save(cartItem);
        return mapperToCartItemResponse(cartItem);
    }

    public boolean deleteCartItem(UUID userId, UUID serviceId) {
        CartItem cartItem = cartRepository.findByUser_IdAndServicePackage_Id(userId, serviceId);
        if (cartItem != null) {
            cartRepository.delete(cartItem);
        }
        return true;
    }

    public CartItemResponse changeServicePackage(UUID userId, UpdateCartItemRequest request) {
        CartItem cartItem = cartRepository.findByUser_IdAndServicePackage_Id(userId, request.oldServiceId());
        if (cartItem == null) throw new NoSuchElementException("Not existing service package!");

        cartItem.setServicePackage(servicePackageRepository.findById(request.newServiceId()).orElseThrow(
                () -> new NoSuchElementException("Not existing service package!")
        ));

        cartItem = cartRepository.save(cartItem);
        return mapperToCartItemResponse(cartItem);
    }

}
