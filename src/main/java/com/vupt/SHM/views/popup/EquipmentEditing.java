package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.EquipmentStatus;
import com.vupt.SHM.constant.SuggestionList;
import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.EquipmentSavingDto;
import com.vupt.SHM.entity.Category;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.services.CategoryService;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EquipmentService;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;
import com.vupt.SHM.views.component.EquipmentStatusListCell;
import com.vupt.SHM.views.popup.SavingPopupController;

import java.util.Optional;
import java.util.function.Consumer;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class EquipmentEditing  implements SavingPopupController<EquipmentSavingDto> {
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private MapstructMapper mapstructMapper;
    @Autowired
    private DepartmentConverter departmentConverter;
    @FXML
    Label lbTitle;
    @FXML
    TextField tfId;
    @FXML
    TextField tfName;
    @FXML
    TextField tfCode;
    @FXML
    TextField tfYear;
    @FXML
    ComboBox<EquipmentStatus> cbStatus;

    public static void loadView(Consumer<EquipmentSavingDto> saveHandler) {
        /*  81 */
        loadView(null, saveHandler);
    }

    @FXML
    Spinner<Integer> spinnerCount;
    @FXML
    TextField tfPrice;
    @FXML
    ComboBox<CategoryDto> cbCategory;
    @FXML
    TextField tfOwner;
    @FXML
    ComboBox<DepartmentDto> cbDepartment;
    @FXML
    TextArea taNote;
    @FXML
    Label lbMessage;
    @FXML
    Button btnSave;
    @FXML
    Button btnGenerateCode;
    @FXML
    private ListView<String> suggestionEquipmentNameListView;
    private Consumer<EquipmentSavingDto> saveHandler;
    private EquipmentSavingDto equipmentSavingDto;

    public static void loadView(EquipmentSavingDto equipmentSavingDto, Consumer<EquipmentSavingDto> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.popup.EquipmentEditing.class.getResource("/com.vupt.SHM.views/popup/EquipmentEdit.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));
            EquipmentEditing equipmentEdit = loader.getController();
            equipmentEdit.init(equipmentSavingDto, saveHandler);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init(EquipmentSavingDto equipmentSavingDto, Consumer<EquipmentSavingDto> saveHandler) {
        this.saveHandler = saveHandler;

        this.cbStatus.setCellFactory(param -> new EquipmentStatusListCell());
        this.cbStatus.setButtonCell((ListCell<EquipmentStatus>) new EquipmentStatusListCell());
        this.tfId.setEditable(false);
        this.tfCode.setEditable(false);
        this.btnGenerateCode.setVisible(false);

        AutoCompletionBinding<String> autoComplete = TextFields.bindAutoCompletion(this.tfName, SuggestionList.equipmentNameSuggestionList);
        autoComplete.setPrefWidth(this.tfName.getPrefWidth());

        this.tfName.textProperty().addListener((observable, oldValue, newValue) -> {
            Optional<Category> res = this.categoryService.findCategoryByMatchName(newValue);
            if (res.isPresent()) {
                this.cbCategory.setValue(this.mapstructMapper.categoryToCategoryDto(res.get()));
            } else {
                this.cbCategory.setValue((CategoryDto) null);
            }
        });
        this.cbDepartment.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        AutoCompleteBox.build(this.cbDepartment, this.taNote);

        this.cbStatus.getItems().addAll(EquipmentStatus.values());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 500);
        valueFactory.setValue(Integer.valueOf(1));
        this.spinnerCount.setValueFactory(valueFactory);
        this.cbCategory.getItems().addAll(this.categoryService.findAllDtoIgnoreSuspended());
        this.cbDepartment.getItems().addAll(this.departmentService.findAllDtoIgnoreSuspended());
        if (equipmentSavingDto == null) {
            this.equipmentSavingDto = new EquipmentSavingDto();
            this.lbTitle.setText("Thêm thiết bị mới");
            this.cbStatus.setValue(EquipmentStatus.USED);
        } else {
            this.equipmentSavingDto = equipmentSavingDto;
            this.lbTitle.setText("Cập nhật thiết bị");
            this.btnGenerateCode.setVisible(true);
            this.cbDepartment.setDisable(true);
            setValueToForm();
        }
    }

    @FXML
    private void generateCode() {
        String generatedCode = this.equipmentSavingDto.generateCode();
        this.tfCode.setText(generatedCode);
    }


    @FXML
    public void save() {
        if (!checkValidation())
            return;
        try {
            this.equipmentSavingDto = getValueFromForm();
            this.saveHandler.accept(this.equipmentSavingDto);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }


    @FXML
    public void close() {
        this.btnSave.getScene().getWindow().hide();
    }


    public EquipmentSavingDto getValueFromForm() {
        EquipmentSavingDto equipmentSavingDto = new EquipmentSavingDto();
        String strId = this.tfId.getText().trim();
        equipmentSavingDto.setId(strId.isEmpty() ? 0L : Integer.parseInt(strId));
        equipmentSavingDto.setName(this.tfName.getText());
        equipmentSavingDto.setCode(this.tfCode.getText());
        if (this.tfYear.getText().trim().equals("")) {
            equipmentSavingDto.setYear(-1);
        } else {
            equipmentSavingDto.setYear(Integer.valueOf(this.tfYear.getText()).intValue());
        }
        equipmentSavingDto.setStatus(this.cbStatus.getValue());
        equipmentSavingDto.setCount(((Integer) this.spinnerCount.getValueFactory().getValue()).intValue());
        if (this.tfPrice.getText().trim().equals("")) {
            equipmentSavingDto.setPrice(-1L);
        } else {
            equipmentSavingDto.setPrice(Long.valueOf(this.tfPrice.getText()).longValue());
        }
        equipmentSavingDto.setCategory(this.cbCategory.getValue());
        equipmentSavingDto.setOwner(this.tfOwner.getText());
        equipmentSavingDto.setDepartment(this.cbDepartment.getValue());
        equipmentSavingDto.setNote(this.taNote.getText());
        return equipmentSavingDto;
    }


    public void setValueToForm() {
        this.tfId.setText(String.valueOf(this.equipmentSavingDto.getId()));
        this.tfName.setText(this.equipmentSavingDto.getName());
        this.tfCode.setText(this.equipmentSavingDto.getCode());
        this.tfYear.setText(String.valueOf(this.equipmentSavingDto.getYear()));
        this.cbStatus.setValue(this.equipmentSavingDto.getStatus());
        this.spinnerCount.getValueFactory().setValue(Integer.valueOf(this.equipmentSavingDto.getCount()));
        this.tfPrice.setText(String.valueOf(this.equipmentSavingDto.getPrice()));
        this.cbCategory.setValue(this.equipmentSavingDto.getCategory());
        this.tfOwner.setText(this.equipmentSavingDto.getOwner());
        this.cbDepartment.setValue(this.equipmentSavingDto.getDepartment());
        this.taNote.setText(this.equipmentSavingDto.getNote());
    }


    public boolean checkValidation() {
        if (this.tfName.getText().trim().isEmpty()) {
            this.lbMessage.setText("Tên thiết bị không được để trống");
            return false;
        }
        if (this.cbStatus.getValue() == null) {
            this.lbMessage.setText("Trạng thái không được để trống");
            return false;
        }
        if (this.cbCategory.getValue() == null) {
            this.lbMessage.setText("Danh mục không đươc để trống");
            return false;
        }
        if (this.cbDepartment.getValue() == null) {
            this.lbMessage.setText("Địa điểm không được để trống");
            return false;
        }
        return true;
    }
}