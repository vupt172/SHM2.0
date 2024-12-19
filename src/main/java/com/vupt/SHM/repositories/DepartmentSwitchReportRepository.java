package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.DepartmentSwitchReport;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentSwitchReportRepository extends JpaRepository<DepartmentSwitchReport, Long> {
    @EntityGraph(attributePaths = {"departmentSwitchReportDetailList"})
    Optional<DepartmentSwitchReport> findDepartmentSwitchReportWithDetailsById(long paramLong);

    @EntityGraph(attributePaths = {"departmentSwitchReportFileList"})
    Optional<DepartmentSwitchReport> findDepartmentSwitchReportWithFilesById(long paramLong);

    List<DepartmentSwitchReport> findAll(Specification<DepartmentSwitchReport> paramSpecification);
}
