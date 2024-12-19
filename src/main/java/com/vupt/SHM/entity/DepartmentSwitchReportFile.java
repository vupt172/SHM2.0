package com.vupt.SHM.entity;

import com.vupt.SHM.entity.BaseEntity;
import com.vupt.SHM.entity.DepartmentSwitchReport;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class DepartmentSwitchReportFile extends BaseEntity<String> {
    private String fileName;
    private String remotePath;
    @ManyToOne
    @JoinColumn(name = "department_switch_report_id", nullable = false)
    private DepartmentSwitchReport departmentSwitchReport;


}

