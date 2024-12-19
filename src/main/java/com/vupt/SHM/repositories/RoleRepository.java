package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.Role;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll(Specification<Role> paramSpecification);

    @EntityGraph(attributePaths = {"authorities"})
    Optional<Role> findById(long paramLong);
}
