package com.vupt.SHM.dto;

import lombok.Data;

@Data
public class DepartmentSwitchReportDetailDto {
    private long id;
    private EquipmentDto equipmentDto;
    private String type;
    private int count;
    private String note;
    private String equipmentStatus;

}
