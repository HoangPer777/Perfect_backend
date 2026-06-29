package com.perfectmarket.modules.service;

import com.perfectmarket.modules.auth.UserPrincipal;
import com.perfectmarket.modules.service.dto.request.CreateServicePackageRequest;
import com.perfectmarket.modules.service.dto.request.UpdateServicePackageRequest;
import com.perfectmarket.modules.service.dto.response.ServicePackageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.perfectmarket.modules.service.dto.response.DesignerServiceGroupResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServicePackageController {
    private final ServicePackageService servicePackageService;

    @GetMapping
    public ResponseEntity<List<ServicePackageResponse>> getAllActivePackages() {
        return ResponseEntity.ok(servicePackageService.getAllActivePackages());
    }
    @GetMapping("/designers")
    public ResponseEntity<List<DesignerServiceGroupResponse>> getDesignerServiceGroups() {
        return ResponseEntity.ok(servicePackageService.getDesignerServiceGroups());
    }
    @GetMapping("/designers/{designerId}")
    public ResponseEntity<List<ServicePackageResponse>> getAllPackagesByDesigner(
            @PathVariable UUID designerId
    ) {
        return ResponseEntity.ok(servicePackageService.getAllPackagesByDesigner(designerId));
    }

    @GetMapping("/get")
    public ResponseEntity<List<ServicePackageResponse>> getServicePackageByProductId(@RequestParam UUID productId) {
        return ResponseEntity.ok(servicePackageService.getServicePackagesByProductId(productId));
    }

    @GetMapping("/my-packages/{productId}")
    public ResponseEntity<List<ServicePackageResponse>> getMyPackages(@PathVariable UUID productId, Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(servicePackageService.getMyPackages(productId, principal.id()));
    }

    @GetMapping("/my-packages")
    public ResponseEntity<List<ServicePackageResponse>> getMyPackages(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(servicePackageService.getDesignerPackages(principal.id()));
    }

    @PostMapping("/create")
    public ResponseEntity<ServicePackageResponse> createServicePackage(
            Authentication authentication,
            @RequestBody CreateServicePackageRequest request
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        ServicePackageResponse response = servicePackageService.create(principal.id(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicePackageResponse> updateServicePackage(
            @PathVariable UUID id,
            @RequestBody UpdateServicePackageRequest request
    ) {
        ServicePackageResponse response = servicePackageService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServicePackage(@PathVariable UUID id) {
        servicePackageService.delete(id);
        return ResponseEntity.noContent().build();
    }


}

