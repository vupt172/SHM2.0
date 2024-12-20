package com.vupt.SHM.specifications;

import com.vupt.SHM.entity.EquipmentHistory;
import com.vupt.SHM.entity.EquipmentRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Date;

public class EquipmentRequestSpecification {
    public static Specification<EquipmentRequest> filterSearch(long departmentId, Date startDate, Date endDate) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            root.join("department");
            Predicate predicate = criteriaBuilder.conjunction();
            if (departmentId != 0)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("department").get("id"), departmentId));
            if (startDate != null && endDate != null)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("date"), startDate, endDate));
            else if (startDate != null)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate));
            else if (endDate != null)
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
            return predicate;
        };
    }
}
