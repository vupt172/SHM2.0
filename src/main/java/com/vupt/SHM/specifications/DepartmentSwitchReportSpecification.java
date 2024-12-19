package com.vupt.SHM.specifications;

import com.vupt.SHM.entity.DepartmentSwitchReport;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class DepartmentSwitchReportSpecification {
    public static Specification<DepartmentSwitchReport> filterSearch(Long departmentFromId, Long departmentToId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (departmentFromId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("departmentFrom").get("id"), departmentFromId));
            if (departmentToId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("departmentTo").get("id"), departmentToId));
            return predicate;
        };
    }
}
