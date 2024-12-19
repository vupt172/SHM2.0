package com.vupt.SHM.entity;

import com.vupt.SHM.entity.BaseEntity;
import com.vupt.SHM.entity.Department;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Employee extends BaseEntity<String> {
    private String username;
    @Column(nullable = false)
    private String fullName;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    private String contact;

    private boolean isManager = false;

}

