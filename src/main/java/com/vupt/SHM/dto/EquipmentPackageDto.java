package com.vupt.SHM.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EquipmentPackageDto {
    private long id;
    private String name;
    private String detail;
    private long price;
    private Date date;
    private String equipmentCodeList;
    @Override
    public String toString() {
        return name;
    }
}