package com.vupt.SHM.repositories;

import com.vupt.SHM.dto.EquipmentPackageDto;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.EquipmentPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipmentPackageRepository extends JpaRepository<EquipmentPackage,Long> {
    boolean existsByName(String name);
    Optional<EquipmentPackage> findByName(String name);
}
