package com.vupt.SHM.dto;

import com.vupt.SHM.constant.DepartmentType;
import lombok.Data;

@Data
public class DepartmentDto {
    private long id;
    private String name;
    private String code;
    private DepartmentType type;
    private boolean isSuspended;
    private DepartmentDto parent;

    @Override
    public String toString() {
        return name;
    }
}