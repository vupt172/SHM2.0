package com.vupt.SHM.entity;

import com.vupt.SHM.constant.DepartmentSwitchReason;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class DepartmentSwitchReport extends BaseEntity<String> {
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "department_from_id", nullable = false)
    private Department departmentFrom;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "department_to_id", nullable = false)
    private Department departmentTo;
    private Date switchDate;
    @Column(nullable = false)
    private DepartmentSwitchReason departmentSwitchReason;
    private String message;

    private boolean isFlush = false;
    private String BEN_A;
    private String DIA_CHI_A;
    private String DAI_DIEN_A;
    private String CHUC_DANH_A;
    private String BEN_B;
    private String DIA_CHI_B;
    private String DAI_DIEN_B;
    private String CHUC_DANH_B;
    private String NOI_DIEN_RA;

    @OneToMany(mappedBy = "departmentSwitchReport", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.SUBSELECT)
    private List<DepartmentSwitchReportDetail> departmentSwitchReportDetailList = new ArrayList<>();

    @OneToMany(mappedBy = "departmentSwitchReport", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    @Fetch(FetchMode.SUBSELECT)
    private List<DepartmentSwitchReportFile> departmentSwitchReportFileList = new ArrayList<>();
}
