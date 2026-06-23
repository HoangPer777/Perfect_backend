package com.perfectmarket.modules.service;

import com.perfectmarket.modules.auth.User;
import com.perfectmarket.modules.auth.UserRepository;
import com.perfectmarket.modules.product.Product;
import com.perfectmarket.modules.product.ProductRepository;
import com.perfectmarket.modules.service.dto.request.CreateServicePackageRequest;
import com.perfectmarket.modules.service.dto.request.UpdateServicePackageRequest;
import com.perfectmarket.modules.service.dto.response.ServicePackageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicePackageService {
    private final ServicePackageRepository servicePackageRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public List<ServicePackageResponse> getServicePackagesByProductId(UUID productId) {
        return servicePackageRepository.findAllByProduct_Id(productId).stream().map(s -> new ServicePackageResponse(
                s.getId(), s.getTitle(), s.getDescription(), s.getPackageType(), s.getPrice(), s.getDeliveryDays(), s.getRevisionsLimit()
        )).toList();
    }

    public List<ServicePackageResponse> getMyPackages(UUID productId, UUID designerId) {
        return servicePackageRepository.findAllByProduct_IdAndDesigner_IdAndStatus(productId, designerId, "ACTIVE")
                .stream()
                .map(p -> new ServicePackageResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getDescription(),
                        p.getPackageType(),
                        p.getPrice(),
                        p.getDeliveryDays(),
                        p.getRevisionsLimit()
                ))
                .toList();
    }

    public ServicePackageResponse create(
            UUID designerId,
            CreateServicePackageRequest request
    ) {
        User designer = userRepository.findById(designerId)
                .orElseThrow(() -> new RuntimeException("Designer not found"));

        Product product = productRepository.findByIdAndIsActiveAndDesigner_Id(request.productId(), true, designerId);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        ServicePackage service = new ServicePackage();
        service.setDesigner(designer);
        service.setProduct(product);
        service.setTitle(request.title());
        service.setDescription(request.description());
        service.setPackageType(request.packageType());
        service.setPrice(request.price());
        service.setDeliveryDays(request.deliveryDays());
        service.setRevisionsLimit(request.revisionsLimit());
        service.setStatus("ACTIVE");

        servicePackageRepository.save(service);

        return new ServicePackageResponse(
                service.getId(),
                service.getTitle(),
                service.getDescription(),
                service.getPackageType(),
                service.getPrice(),
                service.getDeliveryDays(),
                service.getRevisionsLimit()
        );
    }

    public ServicePackageResponse update(
            UUID id,
            UpdateServicePackageRequest request
    ) {
        ServicePackage service = servicePackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service package not found"));

        service.setTitle(request.title());
        service.setDescription(request.description());
        service.setPackageType(request.packageType());
        service.setPrice(request.price());
        service.setDeliveryDays(request.deliveryDays());
        service.setRevisionsLimit(request.revisionsLimit());

        servicePackageRepository.save(service);

        return new ServicePackageResponse(
                service.getId(),
                service.getTitle(),
                service.getDescription(),
                service.getPackageType(),
                service.getPrice(),
                service.getDeliveryDays(),
                service.getRevisionsLimit()
        );
    }

    public void delete(UUID id) {
        servicePackageRepository.deleteById(id);
    }
}
