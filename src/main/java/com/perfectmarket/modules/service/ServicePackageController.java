package com.perfectmarket.modules.service;

import com.perfectmarket.modules.service.dto.response.ServicePackageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServicePackageController {
    private final ServicePackageService servicePackageService;

    @GetMapping("/get")
    public ResponseEntity<List<ServicePackageResponse>> getServicePackageByProductId(@RequestParam UUID productId) {
        return ResponseEntity.ok(servicePackageService.getServicePackagesByProductId(productId));
    }

}
