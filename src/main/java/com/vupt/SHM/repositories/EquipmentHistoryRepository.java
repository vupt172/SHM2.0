package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.EquipmentHistory;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentHistoryRepository extends JpaRepository<EquipmentHistory, Long> {
    List<EquipmentHistory> findAll(Specification<EquipmentHistory> paramSpecification);
}
