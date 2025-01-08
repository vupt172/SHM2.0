package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.entity.Equipment;
import com.vupt.SHM.reports.EquipmentByDepartmentExcelExporter;
import com.vupt.SHM.reports.EquipmentExcelExporter;
import com.vupt.SHM.repositories.EquipmentRepository;
import com.vupt.SHM.services.CategoryService;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.specifications.EquipmentSpecification;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.utils.FileUtils;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EquipmentReportGeneralController {
    @Autowired
    EquipmentByDepartmentExcelExporter equipmentReportExcel;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentConverter departmentConverter;
    @FXML
    TextField tfFolder;
    @FXML
    TextField tfFilePath;
    @FXML
    ComboBox<CategoryDto> cbCategory;
    @FXML
    ComboBox<DepartmentDto> cbDepartment;
    @FXML
    Button btnExport;

    public static Parent loadView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.EquipmentReportGeneralController.class.getResource("/com.vupt.SHM.views/EquipmentReportGeneral.fxml"));
        loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
        Parent view = loader.<Parent>load();
        EquipmentReportGeneralController equipmentReportController = loader.getController();
        return view;
    }

    @FXML
    public void initialize() {
        this.cbCategory.getItems().addAll(this.categoryService.findAllDtoIgnoreSuspended());
        this.cbDepartment.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        AutoCompleteBox.build(this.cbDepartment, this.btnExport);
        this.cbDepartment.getItems().addAll(this.departmentService.findAllDtoIgnoreSuspended());
    }

    @FXML
    public void selectFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a Directory");
        File selectedDirectory = directoryChooser.showDialog(this.tfFolder.getScene().getWindow());
        if (selectedDirectory != null) {
            this.tfFolder.setText(selectedDirectory.getAbsolutePath());
        } else {
            System.out.println("No directory selected.");
        }
    }

    @FXML
    public void clear() {
        this.tfFolder.setText("");
        this.tfFilePath.setText("");
        this.cbCategory.setValue(null);
    }


    @FXML
    public void exportExcel() throws IOException {
        String filePath = String.format("%s\\%s.xlsx",this.tfFolder.getText(), DateTimeUtils.includeTimeToString("Báo cáo thiết bị tổng hợp"));
        this.tfFilePath.setText(filePath);

        CategoryDto categoryDto = this.cbCategory.getValue();
        DepartmentDto departmentDto = this.cbDepartment.getValue();
        long categoryId = (categoryDto == null) ? 0L : categoryDto.getId();
        long departmentId = (departmentDto == null) ? 0L : departmentDto.getId();
        List<Equipment> exportEquipmentList = this.equipmentRepository.findAll(EquipmentSpecification.filterSearch("", "", null, Long.valueOf(categoryId), Long.valueOf(departmentId)));
        EquipmentExcelExporter.writeExcel(exportEquipmentList, this.tfFilePath.getText());

        FileUtils.openFile(tfFilePath.getText());
    }
}
