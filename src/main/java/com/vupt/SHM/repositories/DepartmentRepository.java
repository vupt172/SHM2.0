package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.Department;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String paramString);

    boolean existsByCode(String paramString);

    List<Department> findAll(Specification<Department> paramSpecification);

    List<Department> findByParent_id(long paramLong);

    @Query("SELECT d FROM Department  d WHERE d.parent IS NULL ")
    List<Department> findByRootDepartment();

    Optional<Department> findByName(String paramString);
}


