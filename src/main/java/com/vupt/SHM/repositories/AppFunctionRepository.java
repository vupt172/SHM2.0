package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.AppFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppFunctionRepository extends JpaRepository<AppFunction, Long> {
}

