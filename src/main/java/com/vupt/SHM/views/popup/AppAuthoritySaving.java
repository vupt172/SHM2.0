package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.entity.AppAuthority;
import com.vupt.SHM.entity.AppFunction;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.services.AppFunctionService;
import com.vupt.SHM.views.popup.SavingPopupController;

import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AppAuthoritySaving
        implements SavingPopupController<AppAuthority> {
    @Autowired
    AppFunctionService appFunctionService;
    @FXML
    private Label lbTitle;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfCode;
    @FXML
    private ComboBox<AppFunction> cbAppFunction;
    @FXML
    private Label lbMessage;
    private AppAuthority appAuthority;
    private Consumer<AppAuthority> saveHandler;

    public static void loadView(Consumer<AppAuthority> saveHandler) {
        loadView(null, saveHandler);
    }

    public static void loadView(AppAuthority authority, Consumer<AppAuthority> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(AppAuthoritySaving.class.getResource("/com.vupt.SHM.views/popup/AppAuthorityEdit.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));

            AppAuthoritySaving appAuthoritySaving = loader.getController();
            appAuthoritySaving.init(authority, saveHandler);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(AppAuthority appAuthority, Consumer<AppAuthority> saveHandler) {
        this.saveHandler = saveHandler;
        this.cbAppFunction.setItems(FXCollections.observableList(this.appFunctionService.findAll()));
        this.tfId.setEditable(false);
        if (appAuthority == null) {
            this.appAuthority = new AppAuthority();
            this.lbTitle.setText("New App Authority");
        }
    }


    @FXML
    public void save() {
        if (!checkValidation())
            return;
        try {
            this.appAuthority = getValueFromForm();
            this.saveHandler.accept(this.appAuthority);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }


    @FXML
    public void close() {
        this.lbTitle.getScene().getWindow().hide();
    }


    public AppAuthority getValueFromForm() {
        AppAuthority authority = new AppAuthority();
        if (this.tfId.getText().trim().equals("")) {
            this.appAuthority.setId(0L);
        } else {
            authority.setId(Integer.valueOf(this.tfId.getText()).intValue());
        }

        authority.setName(this.tfName.getText());
        authority.setCode(this.tfCode.getText());
        authority.setAppFunction(this.cbAppFunction.getValue());
        return authority;
    }


    public void setValueToForm() {
    }


    public boolean checkValidation() {
        if (this.tfName.getText().trim().isEmpty()) {
            this.lbMessage.setText("Tên quyền không được để trống");
            return false;
        }
        if (this.tfCode.getText().trim().isEmpty()) {
            this.lbMessage.setText("Code không được để trống");
            return false;
        }
        return true;
    }
}

