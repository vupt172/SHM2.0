package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.reports.EquipmentByDepartmentExcelExporter;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.utils.FileUtils;
import com.vupt.SHM.views.common.CustomAlert;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class EquipmentReportDetailController {
    @Autowired
    EquipmentByDepartmentExcelExporter equipmentReportExcel;
    @FXML
    TextField tfFolder;
    @FXML
    TextField tfFilePath;

    public static Parent loadView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.EquipmentReportDetailController.class.getResource("/com.vupt.SHM.views/EquipmentReportDetail.fxml"));
        loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
        Parent view = loader.<Parent>load();

        com.vupt.SHM.views.EquipmentReportDetailController equipmentReportGeneralController = loader.<com.vupt.SHM.views.EquipmentReportDetailController>getController();
        return view;
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
    public void exportExcel() throws IOException {
        String filePath = String.format("%s\\%s.xlsx", this.tfFolder.getText(), DateTimeUtils.includeTimeToString("Báo cáo thiết bị theo bộ phận"));
        this.tfFilePath.setText(filePath);

        this.equipmentReportExcel.writeExcel(this.tfFilePath.getText());
        FileUtils.openFile(tfFilePath.getText());
    }
}
