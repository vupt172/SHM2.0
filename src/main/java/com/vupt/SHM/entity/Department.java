package com.vupt.SHM.entity;

import com.vupt.SHM.constant.DepartmentType;
import com.vupt.SHM.entity.BaseEntity;
import com.vupt.SHM.entity.Employee;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Data
public class Department extends BaseEntity<String> {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String code = "";
    @Column(nullable = false)
    private DepartmentType type;
    private boolean isSuspended = false;
    @OneToOne
    @JoinColumn(name = "parent_id")
    private Department parent;
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employeeList = new ArrayList<>();
}
