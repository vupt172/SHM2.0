package com.vupt.SHM.specifications;

import com.vupt.SHM.entity.Category;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> filterSearch(String name) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (name != null && !name.isEmpty())
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.like((Expression) root.get("name"), "%" + name + "%"));
            return predicate;
        };
    }
}


