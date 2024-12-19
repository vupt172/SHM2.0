package com.vupt.SHM.repositories;

import com.vupt.SHM.entity.Category;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll(Specification<Category> paramSpecification);

    boolean existsByName(String paramString);

    boolean existsByCode(String paramString);
}

