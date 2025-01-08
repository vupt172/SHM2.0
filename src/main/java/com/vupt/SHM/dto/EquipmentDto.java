package com.vupt.SHM.dto;

import com.vupt.SHM.constant.EquipmentStatus;
import lombok.Data;

import java.util.Date;

@Data
public class EquipmentDto {
    private long id;
    private String name;
    private String code;
    private int year;
    private EquipmentStatus status;
    private int count;
    private long price;
    private DepartmentDto department;
    private CategoryDto category;
    private String owner;
    private EquipmentPackageDto equipmentPackage;
    private String note;
    private Date createdDate;
    private Date lastModifiedDate;

    @Override
    public String toString() {
        return code + " " + name;
    }
}