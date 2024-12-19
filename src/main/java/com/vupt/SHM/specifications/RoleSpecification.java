package com.vupt.SHM.specifications;

import com.vupt.SHM.entity.Role;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class RoleSpecification {
    public static Specification<Role> filterSearch(String code) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (code != null && !code.isEmpty())
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.like((Expression) root.get("code"), "%" + code + "%"));
            return predicate;
        };
    }
}
