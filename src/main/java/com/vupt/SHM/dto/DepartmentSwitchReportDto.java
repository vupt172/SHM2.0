package com.vupt.SHM.dto;

import com.vupt.SHM.constant.DepartmentSwitchReason;
import lombok.Data;

import java.util.Date;

@Data
public class DepartmentSwitchReportDto {
    private long id;
    private DepartmentDto departmentFrom;
    private DepartmentDto departmentTo;
    private Date switchDate;
    private DepartmentSwitchReason departmentSwitchReason;
    private String message;
    private boolean isFlush;
    private String BEN_A;
    private String DIA_CHI_A;
    private String CHUC_DANH_A;
    private String DAI_DIEN_A;
    private String BEN_B;
    private String DIA_CHI_B;
    private String DAI_DIEN_B;
    private String CHUC_DANH_B;
    private String NOI_DIEN_RA;
}
