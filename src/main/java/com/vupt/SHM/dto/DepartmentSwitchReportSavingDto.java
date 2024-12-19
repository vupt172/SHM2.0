package com.vupt.SHM.dto;

import com.vupt.SHM.dto.DepartmentSwitchReportDetailDto;
import com.vupt.SHM.dto.DepartmentSwitchReportDto;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentSwitchReportSavingDto extends DepartmentSwitchReportDto {
    private List<DepartmentSwitchReportDetailDto> departmentSwitchReportDetailList;

}
