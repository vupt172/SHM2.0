package com.vupt.SHM.reports;

import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.EquipmentDto;
import com.vupt.SHM.reports.ExcelSheetDesigner;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EquipmentService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipmentByDepartmentExcelExporter {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    EquipmentService equipmentService;
    public static final int FIRST_COL = 0;
    public static final int LAST_COL = 12;
    public static final int COLUMN_INDEX_DEPARTMENT = 0;
    public static final int COLUMN_INDEX_SUB_DEPARTMENT = 1;
    public static final int COLUMN_INDEX_STT = 2;
    public static final int COLUMN_INDEX_NAME = 3;
    public static final int COLUMN_INDEX_CODE = 4;
    public static final int COLUMN_INDEX_YEAR = 5;
    public static final int COLUMN_INDEX_STATUS = 6;
    public static final int COLUMN_INDEX_COUNT = 7;
    public static final int COLUMN_INDEX_PRICE = 8;
    public static final int COLUMN_INDEX_CATEGORY = 9;
    public static final int COLUMN_INDEX_DEPARTMENT1 = 12;
    public static final int COLUMN_INDEX_EQUIPMENTPACKAGE = 10;
    public static final int COLUMN_INDEX_NOTE = 11;

    public CellStyle formatNumberCellStyle;
    private int rowIndex = 0;

    private Workbook workbook;
    private Sheet sheet;


    public void writeExcel(String excelFilePath) throws IOException {
        this.rowIndex = 0;
        this.workbook = getWorkbook(excelFilePath);
        this.sheet = this.workbook.createSheet("EquipmentReport");
        this.formatNumberCellStyle = ExcelSheetDesigner.createCellStyleFormatNumber(this.workbook);

        writeHeader(this.sheet, this.rowIndex++);


        List<DepartmentDto> rootDepartmentList = (List<DepartmentDto>) this.departmentService.findAllDtoIgnoreSuspended().stream().filter(departmentDto -> (departmentDto.getParent() == null)).collect(Collectors.toList());

        rootDepartmentList.stream().forEach(departmentDto -> writeDepartment(0, departmentDto));

        setColumnWidth(this.sheet);
        createOutputFile(this.workbook, excelFilePath);
        System.out.println("Done!!!");
    }

    public Workbook getWorkbook(String excelFilePath) {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook();
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
        return workbook;
    }

    private void writeHeader(Sheet sheet, int rowIndex) {
        CellStyle cellStyle = ExcelSheetDesigner.createStyleForHeader(sheet.getWorkbook());
        Row row = sheet.createRow(rowIndex);

        Cell cell = row.createCell(COLUMN_INDEX_DEPARTMENT);
        cell.setCellValue("BỘ PHẬN");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_SUB_DEPARTMENT);
        cell.setCellValue("BỘ PHẬN NHÁNH");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_STT);
        cell.setCellValue("STT");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_NAME);
        cell.setCellValue("TÊN THIẾT BỊ");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_CODE);
        cell.setCellValue("CODE");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_YEAR);
        cell.setCellValue("NĂM");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_STATUS);
        cell.setCellValue("TRẠNG THÁI");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_COUNT);
        cell.setCellValue("SỐ LƯỢNG");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_PRICE);
        cell.setCellValue("ĐƠN GIÁ");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_CATEGORY);
        cell.setCellValue("DANH MỤC");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_DEPARTMENT1);
        cell.setCellValue("BỘ PHẬN");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_EQUIPMENTPACKAGE);
        cell.setCellValue("BỘ THIẾT BỊ");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_NOTE);
        cell.setCellValue("GHI CHÚ");
        cell.setCellStyle(cellStyle);


    }


    public void writeEmptyRow() {
        Row row = this.sheet.createRow(this.rowIndex);
        this.rowIndex++;
    }


    public void writeDepartment(int colStart, DepartmentDto departmentDto) {
        writeDepartmentMergeCell(colStart, departmentDto);

        List<EquipmentDto> equipmentDtoList = this.equipmentService.findByDepartmentId(departmentDto.getId());
        for (int i = 0; i < equipmentDtoList.size(); i++) {
            Row row = this.sheet.createRow(this.rowIndex);
            writeEquipmentData(row, i + 1, equipmentDtoList.get(i));
            this.rowIndex++;
        }
        writeEmptyRow();
        List<DepartmentDto> departmentDtoList = this.departmentService.getChildDepartmentsByIdIgnoreSuspended(departmentDto.getId());
        departmentDtoList.stream().forEach(d -> writeDepartment(colStart + 1, d));
    }


    public void writeDepartmentMergeCell(int colStart, DepartmentDto departmentDto) {
        CellStyle cellStyle = ExcelSheetDesigner.createStyleForDepartmentMerge(this.sheet.getWorkbook());
        this.sheet.addMergedRegion(new CellRangeAddress(this.rowIndex, this.rowIndex, colStart, LAST_COL));
        Row row = this.sheet.createRow(this.rowIndex);
        Cell cell = row.createCell(colStart);
        cell.setCellValue(departmentDto.getName().toUpperCase());
        cell.setCellStyle(cellStyle);
        this.rowIndex++;
    }


    public void writeEquipmentData(Row row, int stt, EquipmentDto equipment) {
        CellStyle cellStyle = ExcelSheetDesigner.createStyleForData(this.workbook);
        Cell cell = row.createCell(2);
        cell.setCellValue(stt);
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellValue(equipment.getName());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        cell.setCellValue(equipment.getCode());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        if (equipment.getYear() >= 0) cell.setCellValue(equipment.getYear());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6);
        cell.setCellValue(equipment.getStatus().getTitle());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        cell.setCellValue(equipment.getCount());
        cell.setCellStyle(cellStyle);
        cell = row.createCell(8);
        if (equipment.getPrice() >= 0L) cell.setCellValue(equipment.getPrice());
        cell.setCellStyle(cellStyle);
        cell.setCellStyle(this.formatNumberCellStyle);
        cell = row.createCell(9);
        cell.setCellValue(equipment.getCategory().getName());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_DEPARTMENT1);
        cell.setCellValue(equipment.getDepartment().getName());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_EQUIPMENTPACKAGE);
        if (equipment.getEquipmentPackage() != null)
            cell.setCellValue(equipment.getEquipmentPackage().getName());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_NOTE);
        cell.setCellValue(equipment.getNote());
        cell.setCellStyle(cellStyle);
    }

    private static void setColumnWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 1000);
        sheet.setColumnWidth(1, 1000);
        sheet.setColumnWidth(2, 2000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(5, 3000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 2000);
        sheet.setColumnWidth(8, 3000);
        sheet.setColumnWidth(9, 4000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 7000);
        sheet.setColumnWidth(12, 5000);
    }


    private void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }
}

