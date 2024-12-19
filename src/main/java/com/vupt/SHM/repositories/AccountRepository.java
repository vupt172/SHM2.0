package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.Account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAll(Specification<Account> paramSpecification);

    @EntityGraph(attributePaths = {"roles"})
    Optional<Account> findAccountWithRolesById(long paramLong);

    Optional<Account> findByUsername(String paramString);

    Optional<Account> findAccountWithRolesByUsername(String paramString);
}

