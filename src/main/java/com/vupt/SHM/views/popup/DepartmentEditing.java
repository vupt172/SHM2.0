package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.DepartmentType;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;
import com.vupt.SHM.views.component.DepartmentTypeListCell;
import com.vupt.SHM.views.popup.SavingPopupController;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class DepartmentEditing
        implements SavingPopupController<DepartmentDto> {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    private DepartmentConverter departmentConverter;
    private Consumer<DepartmentDto> saveHandler;
    private DepartmentDto departmentDTO;
    @FXML
    Label lbTitle;
    @FXML
    TextField tfId;

    public static void loadView(Consumer<DepartmentDto> saveHandler) {
        /*  57 */
        loadView(null, saveHandler);
    }

    @FXML
    TextField tfName;
    @FXML
    TextField tfCode;
    @FXML
    ComboBox<DepartmentType> cbTypeList;
    @FXML
    CheckBox cbIsSuspended;
    @FXML
    Label lbMessage;
    @FXML
    Button btnSave;
    @FXML
    ComboBox<DepartmentDto> cbParent;

    public static void loadView(DepartmentDto departmentDTO, Consumer<DepartmentDto> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.popup.DepartmentEditing.class.getResource("/com.vupt.SHM.views/popup/DepartmentEdit.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));

            DepartmentEditing departmentEdit = loader.getController();
            departmentEdit.init(departmentDTO, saveHandler);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init(DepartmentDto departmentDTO, Consumer<DepartmentDto> saveHandler) {
        this.saveHandler = saveHandler;
        this.tfId.setEditable(false);

        this.cbTypeList.getItems().addAll(DepartmentType.values());
        this.cbTypeList.setCellFactory(param -> new DepartmentTypeListCell());
        this.cbTypeList.setButtonCell((ListCell<DepartmentType>) new DepartmentTypeListCell());

        this.cbParent.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        AutoCompleteBox.build(this.cbParent, this.btnSave);

        if (departmentDTO == null) {
            this.departmentDTO = new DepartmentDto();
            this.lbTitle.setText("Thêm bộ phận mới");
            this.cbParent.getItems().addAll(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        } else {
            this.departmentDTO = departmentDTO;
            this.lbTitle.setText("Cập nhật bộ phận");
            setValueToForm();
        }
    }


    @FXML
    public void save() {
        if (!checkValidation())
            return;
        try {
            this.departmentDTO = getValueFromForm();
            this.saveHandler.accept(this.departmentDTO);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }


    @FXML
    public void close() {
        this.btnSave.getScene().getWindow().hide();
    }


    public DepartmentDto getValueFromForm() {
        DepartmentDto departmentDto = new DepartmentDto();
        String strId = this.tfId.getText().trim();
        departmentDto.setId(strId.isEmpty() ? 0L : Integer.parseInt(strId));
        departmentDto.setName(this.tfName.getText());
        departmentDto.setCode(this.tfCode.getText());
        departmentDto.setType(this.cbTypeList.getValue());
        departmentDto.setSuspended(this.cbIsSuspended.isSelected());
        if (this.cbParent.getValue() == null || ((DepartmentDto) this.cbParent.getValue()).getId() == 0L) {
            departmentDto.setParent(null);
        } else {
            departmentDto.setParent(this.cbParent.getValue());
        }
        return departmentDto;
    }


    public void setValueToForm() {
        this.tfId.setText(String.valueOf(this.departmentDTO.getId()));
        this.tfName.setText(this.departmentDTO.getName());
        this.tfCode.setText(this.departmentDTO.getCode());
        this.cbTypeList.setValue(this.departmentDTO.getType());
        this.cbIsSuspended.setSelected(this.departmentDTO.isSuspended());

        List<DepartmentDto> parentList = (List<DepartmentDto>) this.departmentService.findAllDtoIgnoreSuspended().stream().filter(d -> (d.getId() != this.departmentDTO.getId())).collect(Collectors.toList());
        this.cbParent.getItems().addAll(FXCollections.observableList(parentList));
        this.cbParent.setValue(this.departmentDTO.getParent());
    }


    public boolean checkValidation() {
        if (this.tfName.getText().trim().isEmpty()) {
            this.lbMessage.setText("Tên bộ phận không được để trống");
            return false;
        }
        if (this.cbTypeList.getValue() == null) {
            this.lbMessage.setText("Loại bộ phận không được để trống");
            return false;
        }
        return true;
    }
}
