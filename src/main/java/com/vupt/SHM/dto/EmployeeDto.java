package com.vupt.SHM.dto;

import lombok.Data;

@Data
public class EmployeeDto {
    private long id;
    private String fullName;
    private String username;
    private String contact;
    private boolean isManager;
    private DepartmentDto departmentDto;
}

