package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.RepairterDTO;
import com.vupt.SHM.services.RepairterService;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RepairterController {
    @Autowired
    RepairterService repairterService;
    @FXML
    TableView<RepairterDTO> tbRepairter;
    @FXML
    TableColumn<RepairterDTO, Long> colId;
    @FXML
    TableColumn<RepairterDTO, String> colName;
    @FXML
    TableColumn<RepairterDTO, String> colCompany;
    @FXML
    TableColumn<RepairterDTO, String> colPhone;
    @FXML
    TableColumn<RepairterDTO, String> colNote;
    private String windowTitle = "Quản lý đơn vị sửa chữa";


    public static Parent loadView(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.RepairterController.class.getResource("/com.vupt.SHM.views/Repairter.fxml"));
        loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
        Parent view = loader.<Parent>load();

        com.vupt.SHM.views.RepairterController repairterController = loader.<com.vupt.SHM.views.RepairterController>getController();
        primaryStage.setTitle(repairterController.windowTitle);
        return view;
    }


    @FXML
    public void initialize() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        this.colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        this.colNote.setCellValueFactory(new PropertyValueFactory<>("note"));

        this.tbRepairter.setItems(FXCollections.observableList(this.repairterService.findALl()));
    }

    @FXML
    public void createRepairterView() {
    }
}

