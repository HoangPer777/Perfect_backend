package com.perfectmarket.modules.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, UUID> {
    List<ServicePackage> findAllByProduct_Id(UUID id);
    List<ServicePackage> findAllByProduct_IdAndDesigner_IdAndStatus(UUID productId, UUID designerId, String status);
    List<ServicePackage> findAllByDesigner_IdAndStatus(UUID designerId, String status);
    List<ServicePackage> findAllByStatus(String status);

    // Quản lý dịch vụ: lấy các gói dịch vụ đang active của designer
    List<ServicePackage> findByDesigner_IdAndStatusOrderByPackageTypeAsc(UUID designerId, String status);

    // Quản lý dịch vụ: lấy tất cả gói active để gom theo designer
    List<ServicePackage> findByStatusOrderByDesigner_FullNameAscPackageTypeAsc(String status);
}
