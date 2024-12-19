package com.vupt.SHM.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartmentSwitchReportExDto extends DepartmentSwitchReportDto {
    private List<DepartmentSwitchReportDetailDto> departmentSwitchReportDetailList;
    private List<DepartmentSwitchReportFileDto> departmentSwitchReportFileList;
}
