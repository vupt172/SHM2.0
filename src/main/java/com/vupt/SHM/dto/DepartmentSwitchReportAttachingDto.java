package com.vupt.SHM.dto;

import com.vupt.SHM.dto.DepartmentSwitchReportFileDto;
import lombok.Data;

import java.util.List;

@Data
public class DepartmentSwitchReportAttachingDto extends DepartmentSwitchReportDto {
    private List<DepartmentSwitchReportFileDto> departmentSwitchReportFileList;

}


