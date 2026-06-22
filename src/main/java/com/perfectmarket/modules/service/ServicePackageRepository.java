package com.perfectmarket.modules.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, UUID> {
    List<ServicePackage> findAllByProduct_Id(UUID id);
    List<ServicePackage> findAllByDesigner_Id(UUID designerId);
}
