package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.entity.Role;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.views.popup.SavingPopupController;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

@Controller
public class RoleSaving implements SavingPopupController<Role> {
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbMessage;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfCode;
    private Role role;
    private Consumer<Role> saveHandler;

    public static void loadView(Consumer<Role> saveHandler) {
        /*  35 */
        loadView(null, saveHandler);
    }

    public static void loadView(Role role, Consumer<Role> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.popup.RoleSaving.class.getResource("/com.vupt.SHM.views/popup/RoleEdit.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));

            RoleSaving roleSaving = loader.getController();
            roleSaving.init(role, saveHandler);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(Role role, Consumer<Role> saveHandler) {
        this.saveHandler = saveHandler;
        this.tfId.setEditable(false);
        if (role == null) {
            this.role = new Role();
            this.lbTitle.setText("New Role");
        }
    }


    @FXML
    public void save() {
        if (!checkValidation())
            return;
        try {
            this.role = getValueFromForm();
            this.saveHandler.accept(this.role);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }


    @FXML
    public void close() {
        this.lbTitle.getScene().getWindow().hide();
    }


    public Role getValueFromForm() {
        Role role = new Role();
        String strId = this.tfId.getText().trim();
        role.setId(strId.isEmpty() ? 0L : Integer.parseInt(strId));
        role.setName(this.tfName.getText());
        role.setCode(this.tfCode.getText());
        return role;
    }


    public void setValueToForm() {
    }


    public boolean checkValidation() {
        if (this.tfName.getText().trim().isEmpty()) {
            this.lbMessage.setText("Tên vai trò không được để trống");
            return false;
        }
        if (this.tfCode.getText().trim().isEmpty()) {
            this.lbMessage.setText("Code không được để trống");
            return false;
        }
        return true;
    }
}