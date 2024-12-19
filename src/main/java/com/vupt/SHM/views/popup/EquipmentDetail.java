package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.EquipmentDto;
import com.vupt.SHM.views.popup.EmployeeEditing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

@Controller
public class EquipmentDetail {
    @FXML
    TextField tfId;
    @FXML
    TextField tfName;
    @FXML
    TextField tfCode;
    @FXML
    TextField tfYear;
    @FXML
    TextField tfStatus;
    @FXML
    TextField tfCount;
    @FXML
    TextField tfPrice;
    @FXML
    TextField tfCategory;
    @FXML
    TextField tfOwner;
    @FXML
    TextField tfDepartment;
    @FXML
    TextArea taNote;

    public static void loadView(EquipmentDto equipmentDTO) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(EmployeeEditing.class.getResource("/com.vupt.SHM.views/popup/EquipmentDetail.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));
            stage.setTitle(equipmentDTO.toString());
            EquipmentDetail equipmentDetail = loader.getController();
            equipmentDetail.init(equipmentDTO);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(EquipmentDto equipmentDTO) {
        setFormValueFromDTO(equipmentDTO);
    }

    private void setFormValueFromDTO(EquipmentDto equipmentDTO) {
        this.tfId.setText(String.valueOf(equipmentDTO.getId()));
        this.tfName.setText(equipmentDTO.getName());
        this.tfCode.setText(equipmentDTO.getCode());
        this.tfYear.setText(String.valueOf(equipmentDTO.getYear()));
        this.tfStatus.setText(equipmentDTO.getStatus().getTitle());
        this.tfCount.setText(String.valueOf(equipmentDTO.getCount()));
        this.tfPrice.setText(String.valueOf(equipmentDTO.getPrice()));
        this.tfCategory.setText(equipmentDTO.getCategory().getName());
        this.tfOwner.setText(equipmentDTO.getOwner());
        this.tfDepartment.setText(equipmentDTO.getDepartment().getName());
        this.taNote.setText(equipmentDTO.getNote());
    }
}

