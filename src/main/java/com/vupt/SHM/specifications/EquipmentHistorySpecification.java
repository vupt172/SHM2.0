package com.vupt.SHM.specifications;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.vupt.SHM.entity.EquipmentHistory;
import org.springframework.data.jpa.domain.Specification;

public class EquipmentHistorySpecification {
    public static Specification<EquipmentHistory> filterSearch(String code, Long departmentFromId, Long departmentToId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.join("equipment");
            Predicate predicate = criteriaBuilder.conjunction();
            if (code != null && !code.isEmpty())
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.like((Expression) root.get("equipment").get("code"), code));
            if (departmentFromId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("fromDepartment").get("id"), departmentFromId));
            if (departmentToId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("toDepartment").get("id"), departmentToId));
            return predicate;
        };
    }
}

