package com.vupt.SHM.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class DepartmentSwitchReportDetail extends BaseEntity<String> {
    @ManyToOne
    @JoinColumn(name = "department_switch_report_id", nullable = false)
    private DepartmentSwitchReport departmentSwitchReport;
    @ManyToOne
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;
    private String type;
    private int count;
    private String equipmentStatus;
    private String note;
}
