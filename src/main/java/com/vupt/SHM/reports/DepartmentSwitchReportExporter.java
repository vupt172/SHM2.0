package com.vupt.SHM.reports;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.entity.DepartmentSwitchReport;
import com.vupt.SHM.utils.DateTimeUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class DepartmentSwitchReportExporter {
    public static void exportReport(DepartmentSwitchReport departmentSwitchReport) {
        try {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", Long.valueOf(departmentSwitchReport.getId()));
            parameters.put("departmentSwitchReason", departmentSwitchReport.getDepartmentSwitchReason().getReason());
            parameters.put("BEN_A", departmentSwitchReport.getBEN_A());
            parameters.put("DIA_CHI_A", departmentSwitchReport.getDIA_CHI_A());
            parameters.put("DAI_DIEN_A", departmentSwitchReport.getDAI_DIEN_A());
            parameters.put("CHUC_DANH_A", departmentSwitchReport.getCHUC_DANH_A());
            parameters.put("BEN_B", departmentSwitchReport.getBEN_B());
            parameters.put("DIA_CHI_B", departmentSwitchReport.getDIA_CHI_B());
            parameters.put("DAI_DIEN_B", departmentSwitchReport.getDAI_DIEN_B());
            parameters.put("CHUC_DANH_B", departmentSwitchReport.getCHUC_DANH_B());
            parameters.put("switchDate", DateTimeUtils.convertToLocalDateViaSqlDate(departmentSwitchReport.getSwitchDate()));
            parameters.put("NOI_DIEN_RA", departmentSwitchReport.getNOI_DIEN_RA());
            parameters.put("subReport", getSubReport());
            parameters.put("subDataSource", getSubDataSource(departmentSwitchReport));

            InputStream inputStream = MyApplication.class.getResourceAsStream("/reports/DepartmentSwitchReport.jrxml");
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found!");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JasperPrint jsPrint = JasperFillManager.fillReport(jasperReport, parameters, (JRDataSource) new JREmptyDataSource());

            showReport(jsPrint);
            System.out.println("Report created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showReport(JasperPrint jsPrint) {
        JasperViewer.viewReport(jsPrint, false);
    }


    public static JasperReport getSubReport() {
        try {
            InputStream inputStream = MyApplication.class.getResourceAsStream("/reports/DepartmentSwitchReportDetail.jrxml");
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found!");
            }
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);


            return jasperReport;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JRBeanCollectionDataSource getSubDataSource(DepartmentSwitchReport departmentSwitchReport) {
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(departmentSwitchReport.getDepartmentSwitchReportDetailList());
        return dataSource;
    }
}

