package com.vupt.SHM.entity;

import com.vupt.SHM.entity.BaseEntity;
import com.vupt.SHM.entity.Department;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Employee extends BaseEntity<String> {
    private String username;
    @Column(nullable = false)
    private String fullName;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    private String contact;

    private boolean isManager = false;

}

