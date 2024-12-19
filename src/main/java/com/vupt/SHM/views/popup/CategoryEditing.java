package com.vupt.SHM.views.popup;

import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.views.popup.SavingPopupController;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryEditing implements SavingPopupController<CategoryDto> {
    private Consumer<CategoryDto> saveHandler;
    private CategoryDto categoryDTO;
    @FXML
    Label lbTitle;
    @FXML
    TextField tfId;
    @FXML
    TextField tfName;
    @FXML
    TextField tfCode;
    @FXML
    CheckBox cbIsSuspended;
    @FXML
    Label lbMessage;
    @FXML
    Button btnSave;

    public static void loadView(Consumer<CategoryDto> saveHandler) {
        loadView(null, saveHandler);
    }


    public static void loadView(CategoryDto categoryDTO, Consumer<CategoryDto> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(CategoryEditing.class.getResource("/com.vupt.SHM.views/popup/CategoryEdit.fxml"));
            stage.setScene(new Scene(loader.<Parent>load()));
            CategoryEditing categoryEdit = loader.getController();
            categoryEdit.init(categoryDTO, saveHandler);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(CategoryDto categoryDTO, Consumer<CategoryDto> saveHandler) {
        this.saveHandler = saveHandler;
        this.tfId.setEditable(false);
        if (categoryDTO == null) {
            this.categoryDTO = new CategoryDto();
            this.lbTitle.setText("Thêm danh mục mới");
            this.cbIsSuspended.setDisable(true);
        } else {
            this.categoryDTO = categoryDTO;
            this.lbTitle.setText("Cập nhật danh mục");
            setValueToForm();
        }
    }


    @FXML
    public void save() {
        if (!checkValidation()) return;
        try {
            this.categoryDTO = getValueFromForm();
            this.saveHandler.accept(this.categoryDTO);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }


    @FXML
    public void close() {
        this.btnSave.getScene().getWindow().hide();
    }


    public CategoryDto getValueFromForm() {
        CategoryDto categoryDto = new CategoryDto();
        String strId = this.tfId.getText().trim();
        categoryDto.setId(strId.isEmpty() ? 0L : Integer.parseInt(strId));
        categoryDto.setName(this.tfName.getText());
        categoryDto.setCode(this.tfCode.getText());
        categoryDto.setSuspended(this.cbIsSuspended.isSelected());
        return categoryDto;
    }


    public void setValueToForm() {
        this.tfId.setText(String.valueOf(this.categoryDTO.getId()));
        this.tfName.setText(this.categoryDTO.getName());
        this.tfCode.setText(this.categoryDTO.getCode());
        this.cbIsSuspended.setSelected(this.categoryDTO.isSuspended());
    }


    public boolean checkValidation() {
        if (this.tfName.getText().trim().isEmpty()) {
            this.lbMessage.setText("Tên danh mục không được trống");
            return false;
        }
        if (this.tfCode.getText().trim().isEmpty()) {
            this.lbMessage.setText("Code không được trống");
            return false;
        }
        return true;
    }
}

