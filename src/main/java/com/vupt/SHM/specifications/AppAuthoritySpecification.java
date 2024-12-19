package com.vupt.SHM.specifications;

import com.vupt.SHM.entity.AppAuthority;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class AppAuthoritySpecification {
    public static Specification<AppAuthority> filterSearch(Long appFunctionId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.join("appFunction");
            Predicate predicate = criteriaBuilder.conjunction();
            if (appFunctionId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("appFunction").get("id"), appFunctionId));
            return predicate;
        };
    }
}


