package com.vupt.SHM.specifications;

import com.vupt.SHM.constant.EquipmentStatus;
import com.vupt.SHM.entity.Equipment;

import java.lang.invoke.SerializedLambda;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class EquipmentSpecification {
    public static Specification<Equipment> filterSearch(String name, String code,EquipmentStatus status, Long categoryId, Long departmentId) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.join("category");
            root.join("department");
            Predicate predicate = criteriaBuilder.conjunction();
            if (name != null && !name.isEmpty())
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.like((Expression) root.get("name"), "%" + name + "%"));
            if (code != null && !code.isEmpty())
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.like((Expression) root.get("code"), "%" + code + "%"));
            if (status != null)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("status"), status));
            if (categoryId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("category").get("id"), categoryId));
            if (departmentId.longValue() != 0L)
                predicate = criteriaBuilder.and((Expression) predicate, (Expression) criteriaBuilder.equal((Expression) root.get("department").get("id"), departmentId));
            return predicate;
        };
    }
}
