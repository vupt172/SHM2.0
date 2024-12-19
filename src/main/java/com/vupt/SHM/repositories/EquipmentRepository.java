package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.Equipment;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    long countByCategory_Id(long paramLong);

    long countByCategory_IdAndDepartmentId(long paramLong1, long paramLong2);

    List<Equipment> findAll(Specification<Equipment> paramSpecification);

    List<Equipment> findByDepartment_Id(long paramLong);

    List<Equipment> findByCategory_Id(long paramLong);
}

