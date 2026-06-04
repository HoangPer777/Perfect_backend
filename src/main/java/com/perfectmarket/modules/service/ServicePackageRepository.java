package com.perfectmarket.modules.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, UUID> {
}
