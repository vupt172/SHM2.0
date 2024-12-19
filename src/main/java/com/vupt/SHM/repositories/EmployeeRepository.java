package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.Employee;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAll(Specification<Employee> paramSpecification);

    boolean existsByFullName(String paramString);

    boolean existsByUsername(String paramString);

    List<Employee> findByIsManager(Boolean paramBoolean);
}

