package com.vupt.SHM.dto;

import lombok.Data;

@Data
public class DepartmentSwitchReportFileDto {
    private long id;
    private String fileName;
    private String remotePath;
    private String localPath;
}
