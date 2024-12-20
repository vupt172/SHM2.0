package com.vupt.SHM.dto;

import com.vupt.SHM.entity.Department;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
public class EquipmentRequestSavingDto {
    private long id;
    private String name;
    private String solution;
    private Date date;
    private DepartmentDto departmentDto;
    private String result;
    private String note;
}
