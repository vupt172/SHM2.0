package com.vupt.SHM.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
public class EquipmentRequest  extends BaseEntity<String>{
    @Column(nullable = false)
    private String name;
    private String status;
    private String solution;
    private String employeeRequest;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @Column(nullable = false)
    private Date date;
    private String result;
    private String note;
    private boolean isDone;
}
