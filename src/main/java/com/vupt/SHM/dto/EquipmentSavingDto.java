package com.vupt.SHM.dto;

import com.vupt.SHM.constant.EquipmentStatus;
import lombok.Data;

@Data
public class EquipmentSavingDto {
    private long id;
    private String name;
    private String code;
    private int year;
    private EquipmentStatus status;
    private int count;
    private long price;
    private String owner;
    private DepartmentDto department;
    private CategoryDto category;
    private EquipmentPackageDto equipmentPackage;
    private String note;

    public String generateCode() {
        return this.category.getCode() + getId();
    }
}
