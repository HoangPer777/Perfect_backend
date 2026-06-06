package com.perfectmarket.modules.service;

import com.perfectmarket.modules.service.dto.response.ServicePackageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServicePackageService {
    private final ServicePackageRepository servicePackageRepository;

    public List<ServicePackageResponse> getServicePackagesByProductId(UUID productId) {
        return servicePackageRepository.findAllByProduct_Id(productId).stream().map(s -> new ServicePackageResponse(
                s.getId(), s.getTitle(), s.getDescription(), s.getPackageType(), s.getPrice(), s.getDeliveryDays(), s.getRevisionsLimit()
        )).toList();
    }
}
