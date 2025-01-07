package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.EquipmentPackageDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.utils.DateTimeUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;


import java.util.function.Consumer;

@Controller
public class EquipmentPackageSaving implements SavingPopupController<EquipmentPackageDto> {
    @FXML
    Label lbTitle;
    @FXML
    TextField tfId;
    @FXML
    TextField tfName;
    @FXML
    TextArea taDetail;
    @FXML
    TextField tfPrice;
    @FXML
    DatePicker dpDate;
    @FXML
    TextField tfEquipmentCodeList;
    @FXML
    Label lbMessage;
    @FXML
    Button btnSave;
    private Consumer<EquipmentPackageDto> saveHandler;
    private EquipmentPackageDto equipmentPackageDto;


    public static void loadView(Consumer<EquipmentPackageDto> saveHandler) {
        loadView(null, saveHandler);
    }
    public static void loadView(EquipmentPackageDto equipmentPackageDto, Consumer<EquipmentPackageDto> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.popup.EquipmentEditing.class.getResource("/com.vupt.SHM.views/popup/EquipmentPackageSave.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));
            EquipmentPackageSaving equipmentPackageSaving = loader.getController();
            equipmentPackageSaving.init(equipmentPackageDto, saveHandler);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(EquipmentPackageDto equipmentPackageDto, Consumer<EquipmentPackageDto> saveHandler) {
        this.saveHandler=saveHandler;
        this.tfId.setEditable(false);
        if (equipmentPackageDto == null) {
            this.equipmentPackageDto = new EquipmentPackageDto();
            this.lbTitle.setText("Thêm bộ thiết bị mới");
        } else {
            this.equipmentPackageDto = equipmentPackageDto;
            this.lbTitle.setText("Cập nhật bộ thiết bị");
            setValueToForm();
        }
    }

    @Override
    public void save() {
        if (!checkValidation()) return;
        try {
            this.equipmentPackageDto = getValueFromForm();
            this.saveHandler.accept(this.equipmentPackageDto);
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
    public EquipmentPackageDto getValueFromForm() {
        EquipmentPackageDto equipmentPackageDto=new EquipmentPackageDto();
        String strId=tfId.getText().trim();
        equipmentPackageDto.setId(strId.isEmpty()?0:Integer.valueOf(strId));
        equipmentPackageDto.setName(tfName.getText());
        equipmentPackageDto.setDetail(taDetail.getText());
        String strPrice=tfPrice.getText().trim();
        equipmentPackageDto.setPrice(strPrice.isEmpty()?-1:Long.parseLong(strPrice));
        equipmentPackageDto.setDate(DateTimeUtils.asDate(dpDate.getValue()));
        equipmentPackageDto.setEquipmentCodeList(tfEquipmentCodeList.getText());
        return equipmentPackageDto;
    }

    @Override
    public void setValueToForm() {
        tfId.setText(String.valueOf(equipmentPackageDto.getId()));
        tfName.setText(equipmentPackageDto.getName());
        taDetail.setText(equipmentPackageDto.getDetail());
        tfPrice.setText(String.valueOf(equipmentPackageDto.getPrice()));
        dpDate.setValue(DateTimeUtils.convertToLocalDateViaSqlDate(equipmentPackageDto.getDate()));
        tfEquipmentCodeList.setText(equipmentPackageDto.getEquipmentCodeList());
    }

    @Override
    public boolean checkValidation() {
        if(this.tfName.getText().trim().isEmpty()){
            this.lbMessage.setText("Tên không được để trống");
            return false;
        }
        if(this.taDetail.getText().trim().isEmpty()){
            this.lbMessage.setText("Cấu hình chi tiết không được để trống");
            return false;
        }
        if(this.dpDate.getValue()==null){
            this.lbMessage.setText("Ngày không được để trống");
            return false;
        }
        return true;
    }
}
