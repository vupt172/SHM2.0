package com.vupt.SHM.specifications;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.vupt.SHM.entity.Account;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification {
    public static Specification<Account> filterSearch(String fullName) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (fullName != null && !fullName.isEmpty())
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.like((Expression) root.get("fullName"), "%" + fullName + "%"));
            return predicate;
        };
    }
}

