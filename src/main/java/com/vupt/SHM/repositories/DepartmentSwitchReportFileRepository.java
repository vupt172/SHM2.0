package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.DepartmentSwitchReportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentSwitchReportFileRepository extends JpaRepository<DepartmentSwitchReportFile, Long> {
}

