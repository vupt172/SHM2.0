package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.EquipmentRequestSavingDto;
import com.vupt.SHM.entity.Equipment;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.function.Consumer;


@Controller
public class EquipmentRequestSaving implements SavingPopupController<EquipmentRequestSavingDto> {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentConverter departmentConverter;
    @FXML
    Label lbTitle;
    @FXML
    TextField tfId;
    @FXML
    TextField tfName;
    @FXML
    TextField tfSolution;
    @FXML
    DatePicker dpDate;
    @FXML
    TextField tfResult;
    @FXML
    ComboBox<DepartmentDto> cbDepartment;
    @FXML
    TextField tfNote;
    @FXML
    Label lbMessage;

    @FXML
    Button btnSave;

    private EquipmentRequestSavingDto equipmentRequestSavingDto;
    private Consumer<EquipmentRequestSavingDto> saveHandler;

    public static void loadView(Consumer<EquipmentRequestSavingDto> saveHandler) {
        loadView(null, saveHandler);
    }


    public static void loadView(EquipmentRequestSavingDto equipmentRequestSavingDto, Consumer<EquipmentRequestSavingDto> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(EquipmentRequestSaving.class.getResource("/com.vupt.SHM.views/popup/EquipmentRequestSave.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));
            EquipmentRequestSaving equipmentRequestSaving = loader.getController();
            equipmentRequestSaving.init(equipmentRequestSavingDto, saveHandler);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(EquipmentRequestSavingDto equipmentRequestSavingDto, Consumer<EquipmentRequestSavingDto> saveHandler) {
        this.saveHandler = saveHandler;
        this.tfId.setEditable(false);
        cbDepartment.getItems().addAll(departmentService.findAllDto());
        AutoCompleteBox.build(cbDepartment,departmentConverter);
        if (equipmentRequestSavingDto == null) {
            this.equipmentRequestSavingDto = new EquipmentRequestSavingDto();
            this.lbTitle.setText("Thêm giấy đề nghị mới");
        } else {
            this.equipmentRequestSavingDto = equipmentRequestSavingDto;
            this.lbTitle.setText("Cập nhật giấy đề nghị ");
            setValueToForm();
        }
    }

    @Override
    public void save() {
        if (!checkValidation()) return;
        try {
            this.equipmentRequestSavingDto = getValueFromForm();
            this.saveHandler.accept(this.equipmentRequestSavingDto);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }

    @Override
    public void close() {
        btnSave.getScene().getWindow().hide();
    }

    @Override
    public EquipmentRequestSavingDto getValueFromForm() {
        EquipmentRequestSavingDto equipmentRequestSavingDto = new EquipmentRequestSavingDto();
        String strId = tfId.getText().trim();
        equipmentRequestSavingDto.setId(strId.isEmpty() ? 0 : Integer.parseInt(strId));
        equipmentRequestSavingDto.setName(tfName.getText());
        equipmentRequestSavingDto.setSolution(tfSolution.getText());
        equipmentRequestSavingDto.setDate(DateTimeUtils.asDate(dpDate.getValue()));
        equipmentRequestSavingDto.setResult(tfResult.getText());
        equipmentRequestSavingDto.setDepartmentDto(cbDepartment.getValue());
        equipmentRequestSavingDto.setNote(tfNote.getText());
        return equipmentRequestSavingDto;
    }

    @Override
    public void setValueToForm() {
            tfId.setText(String.valueOf(equipmentRequestSavingDto.getId()));
            tfName.setText(equipmentRequestSavingDto.getName());
            tfSolution.setText(equipmentRequestSavingDto.getSolution());
            dpDate.setValue(DateTimeUtils.convertToLocalDateViaSqlDate(equipmentRequestSavingDto.getDate()));
            tfResult.setText(equipmentRequestSavingDto.getResult());
            cbDepartment.setValue(equipmentRequestSavingDto.getDepartmentDto());
            tfNote.setText(equipmentRequestSavingDto.getNote());
    }

    @Override
    public boolean checkValidation() {
        return true;
    }
}
