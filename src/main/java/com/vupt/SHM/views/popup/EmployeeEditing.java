package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.EmployeeDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.views.popup.SavingPopupController;

import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class EmployeeEditing implements SavingPopupController<EmployeeDto> {
    private Consumer<EmployeeDto> saveHandler;
    private Object owner;
    private EmployeeDto employeeDTO;
    @Autowired
    DepartmentService departmentService;
    @FXML
    Label lbTitle;
    @FXML
    private TextField tfId;

    public static void loadView(Consumer<EmployeeDto> saveHandler, Object owner) {
        loadView(null, saveHandler, owner);
    }

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfUsername;
    @FXML
    private TextField tfContact;
    @FXML
    private CheckBox cbIsManager;
    @FXML
    private Label lbMessage;
    @FXML
    private ComboBox<DepartmentDto> cbDepartment;
    @FXML
    private Button btnSave;

    public static void loadView(EmployeeDto employeeDTO, Consumer<EmployeeDto> saveHandler, Object owner) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(EmployeeEditing.class.getResource("/com.vupt.SHM.views/popup/EmployeeEdit.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));

            EmployeeEditing employeeEdit = loader.getController();

            employeeEdit.init(employeeDTO, saveHandler, owner);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init(EmployeeDto employeeDTO, Consumer<EmployeeDto> saveHandler, Object owner) {
        this.saveHandler = saveHandler;
        this.owner = owner;
        this.tfId.setEditable(false);
        this.cbDepartment.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        if (employeeDTO == null) {
            this.lbTitle.setText("Thêm nhân viên mới");
            this.employeeDTO = new EmployeeDto();
        } else {
            this.lbTitle.setText("Cập nhật nhân viên");
            this.employeeDTO = employeeDTO;
            setValueToForm();
        }
    }


    public void save() {
        if (!checkValidation())
            return;
        try {
            this.employeeDTO = getValueFromForm();
            this.saveHandler.accept(this.employeeDTO);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }


    public void close() {
        /* 105 */
        this.btnSave.getScene().getWindow().hide();
    }


    public EmployeeDto getValueFromForm() {
        EmployeeDto employeeDto = new EmployeeDto();
        String strId = this.tfId.getText().trim();
        employeeDto.setId(strId.isEmpty() ? 0L : Integer.parseInt(strId));
        employeeDto.setFullName(this.tfName.getText());
        employeeDto.setUsername(this.tfUsername.getText());
        employeeDto.setDepartmentDto(this.cbDepartment.getValue());
        employeeDto.setContact(this.tfContact.getText());
        employeeDto.setManager(this.cbIsManager.isSelected());
        return employeeDto;
    }


    public void setValueToForm() {
        this.tfId.setText(String.valueOf(this.employeeDTO.getId()));
        this.tfName.setText(this.employeeDTO.getFullName());
        this.tfUsername.setText(this.employeeDTO.getUsername());
        this.cbDepartment.setValue(this.employeeDTO.getDepartmentDto());
        this.tfContact.setText(this.employeeDTO.getContact());
        this.cbIsManager.setSelected(this.employeeDTO.isManager());
    }


    public boolean checkValidation() {
        if (this.tfName.getText().trim().isEmpty()) {
            this.lbMessage.setText("Tên nhân viên không được để trống");
            return false;
        }

        if (this.cbDepartment.getValue() == null) {
            this.lbMessage.setText("Bộ phận không được để trống");
            return false;
        }
        return true;
    }
}

