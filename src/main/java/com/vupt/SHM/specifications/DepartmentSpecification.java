package com.vupt.SHM.specifications;

import com.vupt.SHM.constant.DepartmentType;
import com.vupt.SHM.entity.Department;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

public class DepartmentSpecification {
    public static Specification<Department> containsNameAndTypeIfPresent(String name, DepartmentType type) {
        return (root, query, criteriaBuilder) ->
                (name == null && type == null) ? criteriaBuilder.conjunction() : (
                        (name != null && type != null) ? criteriaBuilder.and((Expression) criteriaBuilder.like((Expression) root.get("name"), "%" + name + "%"), (Expression) criteriaBuilder.equal((Expression) root.get("type"), type)) : ((name != null) ? criteriaBuilder.like((Expression) root.get("name"), "%" + name + "%") : criteriaBuilder.equal((Expression) root.get("type"), type)));
    }
}


