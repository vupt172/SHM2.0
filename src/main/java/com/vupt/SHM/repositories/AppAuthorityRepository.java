package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.AppAuthority;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppAuthorityRepository extends JpaRepository<AppAuthority, Long> {
    List<AppAuthority> findAll(Specification<AppAuthority> paramSpecification);

    List<AppAuthority> findByAppFunction_Id(long paramLong);
}

