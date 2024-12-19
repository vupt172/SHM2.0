package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.DepartmentSwitchReportDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentSwitchReportDetailRepository extends JpaRepository<DepartmentSwitchReportDetail, Long> {
}

