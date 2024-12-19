package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.AccountDto;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;


@Controller
public class AccountEditing  implements SavingPopupController<AccountDto> {
    @Autowired
    PasswordEncoder passwordEncoder;
    private Consumer<AccountDto> saveHandler;
    private AccountDto accountDTO;
    @FXML
    Label lbTitle;
    @FXML
    TextField tfId;
    @FXML
    TextField tfUsername;
    @FXML
    TextField tfFullName;
    @FXML
    PasswordField tfPassword;
    @FXML
    CheckBox cbIsSuspended;
    @FXML
    Label lbMessage;
    @FXML
    Button btnSave;

    public static void loadView(Consumer<AccountDto> saveHandler) {
        loadView(null, saveHandler);
    }

    public static void loadView(AccountDto accountDTO, Consumer<AccountDto> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.popup.AccountEditing.class.getResource("/com.vupt.SHM.views/popup/AccountEdit.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));


            com.vupt.SHM.views.popup.AccountEditing accountEdit = loader.<com.vupt.SHM.views.popup.AccountEditing>getController();
            accountEdit.init(accountDTO, saveHandler);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void init(AccountDto AccountDTO, Consumer<AccountDto> saveHandler) {
        this.saveHandler = saveHandler;
        this.tfId.setEditable(false);
        if (AccountDTO == null) {
            this.accountDTO = new AccountDto();
            this.lbTitle.setText("Thêm tài khoản mới");
            this.cbIsSuspended.setDisable(true);
        } else {
            this.tfUsername.setEditable(false);
            this.tfPassword.setEditable(false);
            this.accountDTO = AccountDTO;
            this.lbTitle.setText("Cập nhật tài khoản");
            this.tfId.setText(String.valueOf(AccountDTO.getId()));
            this.tfUsername.setText(AccountDTO.getUsername());
            this.tfPassword.setText(AccountDTO.getPassword());
            this.tfFullName.setText(AccountDTO.getFullName());
            this.cbIsSuspended.setSelected(AccountDTO.isSuspended());
        }
    }


    @FXML
    public void save() {
        if (!checkValidation())
            return;
        try {
            this.accountDTO = getValueFromForm();
            this.saveHandler.accept(this.accountDTO);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }


    @FXML
    public void close() {
        this.btnSave.getScene().getWindow().hide();
    }


    public AccountDto getValueFromForm() {
        AccountDto accountDto = new AccountDto();
        String strId = this.tfId.getText();
        if (strId.isEmpty()) {
            accountDto.setId(0L);
        } else {
            accountDto.setId(Integer.parseInt(strId));
        }
        accountDto.setUsername(this.tfUsername.getText());
        accountDto.setFullName(this.tfFullName.getText());
        accountDto.setPassword(this.tfPassword.getText());
        accountDto.setSuspended(this.cbIsSuspended.isSelected());
        return accountDto;
    }


    public void setValueToForm() {
    }


    public boolean checkValidation() {
        return true;
    }
}
