package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.Repairter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairterRepository extends JpaRepository<Repairter, Long> {
}
