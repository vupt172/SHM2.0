package com.vupt.SHM.specifications;

import com.vupt.SHM.entity.Equipment;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class EquipmentGroupSpecification {
    public static Specification<Equipment> filterSearc(Long departmentId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.join("department");
            Predicate predicate = criteriaBuilder.conjunction();
            if (departmentId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("department").get("id"), departmentId));
            return predicate;
        };
    }
}

