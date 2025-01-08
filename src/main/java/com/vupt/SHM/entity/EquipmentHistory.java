package com.vupt.SHM.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class EquipmentHistory extends BaseEntity<String> {
    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;
    @ManyToOne
    @JoinColumn(name = "department_from_id")
    private Department fromDepartment;
    @ManyToOne
    @JoinColumn(name = "department_to_id")
    private Department toDepartment;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transportDate;
    private String message;
}

