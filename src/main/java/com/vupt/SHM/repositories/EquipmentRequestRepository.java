package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.Equipment;
import com.vupt.SHM.entity.EquipmentRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRequestRepository  extends JpaRepository<EquipmentRequest,Long> {
    List<EquipmentRequest> findAll(Specification<EquipmentRequest> paramSpecification,Sort sort);
    public List<EquipmentRequest> findByDepartment_Id(long departmentId, Sort sort);
}
