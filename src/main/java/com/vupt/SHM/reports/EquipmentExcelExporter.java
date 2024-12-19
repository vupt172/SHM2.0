package com.vupt.SHM.reports;

import com.vupt.SHM.entity.Equipment;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EquipmentExcelExporter {
    private static CellStyle cellStyleFormatNumber = null;
    public static final int COLUMN_INDEX_ID = 0;
    public static final int COLUMN_INDEX_NAME = 1;
    public static final int COLUMN_INDEX_CODE = 2;
    public static final int COLUMN_INDEX_YEAR = 3;
    public static final int COLUMN_INDEX_STATUS = 4;
    public static final int COLUMN_INDEX_COUNT = 5;
    public static final int COLUMN_INDEX_PRICE = 6;
    public static final int COLUMN_INDEX_CATEGORY = 7;
    public static final int COLUMN_INDEX_DEPARTMENT = 8;
    public static final int COLUMN_INDEX_NOTE = 9;

    public static void writeExcel(List<Equipment> equipmentList, String excelFilePath) throws IOException {
        Workbook workbook = getWorkbook(excelFilePath);
        Sheet sheet = workbook.createSheet("EquipmentReport");
        cellStyleFormatNumber = getCellStyleFormatNumber(workbook);

        int rowIndex = 0;
        writeHeader(sheet, rowIndex);

        rowIndex++;
        for (Equipment equipment : equipmentList) {

            Row row = sheet.createRow(rowIndex);

            writeData(equipment, row);
            rowIndex++;
        }


        setColumnWidth(sheet);
        createOutputFile(workbook, excelFilePath);
        System.out.println("Done!!!");
    }


    private static CellStyle getCellStyleFormatNumber(Workbook workbook) {
        short format = (short) BuiltinFormats.getBuiltinFormat("#,##0");


        CellStyle cellStyleFormatNumber = workbook.createCellStyle();
        cellStyleFormatNumber.setDataFormat(format);

        return cellStyleFormatNumber;
    }

    private static Workbook getWorkbook(String excelFilePath) {
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

    private static void writeHeader(Sheet sheet, int rowIndex) {
        CellStyle cellStyle = createStyleForHeader(sheet);
        Row row = sheet.createRow(rowIndex);

        Cell cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellValue("ID");
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


        cell = row.createCell(COLUMN_INDEX_DEPARTMENT);
        cell.setCellValue("BỘ PHẬN");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_NOTE);
        cell.setCellValue("GHI CHÚ");
        cell.setCellStyle(cellStyle);
    }


    private static void writeData(Equipment equipment, Row row) {
        CellStyle cellStyle = createStyleForData(row.getSheet());
        Cell cell = row.createCell(COLUMN_INDEX_ID);
        cell.setCellValue(equipment.getId());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_NAME);
        cell.setCellValue(equipment.getName());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_CODE);
        cell.setCellValue(equipment.getCode());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_YEAR);
        if (equipment.getYear() >= 0) cell.setCellValue(equipment.getYear());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_STATUS);
        cell.setCellValue(equipment.getStatus().getTitle());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_COUNT);
        cell.setCellValue(equipment.getCount());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_PRICE);
        if (equipment.getPrice() >= 0L) cell.setCellValue(equipment.getPrice());
        cell.setCellStyle(cellStyle);
        cell.setCellStyle(cellStyleFormatNumber);

        cell = row.createCell(COLUMN_INDEX_CATEGORY);
        cell.setCellValue(equipment.getCategory().getName());
        cell.setCellStyle(cellStyle);


        cell = row.createCell(COLUMN_INDEX_DEPARTMENT);
        cell.setCellValue(equipment.getDepartment().getName());
        cell.setCellStyle(cellStyle);

        cell = row.createCell(COLUMN_INDEX_NOTE);
        cell.setCellValue(equipment.getNote());
        cell.setCellStyle(cellStyle);
    }


    private static CellStyle createStyleForHeader(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.BLACK.getIndex());
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        return cellStyle;
    }


    private static CellStyle createStyleForData(Sheet sheet) {
        Font font = sheet.getWorkbook().createFont();
        font.setFontName("Times New Roman");

        font.setColor(IndexedColors.BLACK.getIndex());

        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        cellStyle.setFont(font);

        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return cellStyle;
    }


    private static void autosizeColumn(Sheet sheet, int lastColumn) {
        for (int columnIndex = 0; columnIndex < lastColumn; columnIndex++) {
            sheet.autoSizeColumn(columnIndex);
        }
    }


    private static void setColumnWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(3, 3000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 2000);
        sheet.setColumnWidth(6, 3000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 6000);
        sheet.setColumnWidth(9, 8000);
    }

    private static void createOutputFile(Workbook workbook, String excelFilePath) throws IOException {
        try (OutputStream os = new FileOutputStream(excelFilePath)) {
            workbook.write(os);
        }
    }
}

