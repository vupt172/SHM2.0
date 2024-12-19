package com.vupt.SHM.specifications;

import com.vupt.SHM.entity.Employee;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {
    public static Specification<Employee> filterSearch(String name, Long departmentId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.join("department");
            Predicate predicate = criteriaBuilder.conjunction();
            if (name != null && !name.isEmpty())
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.like((Expression) root.get("fullName"), "%" + name + "%"));
            if (departmentId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("department").get("id"), departmentId));
            return predicate;
        };
    }
}


