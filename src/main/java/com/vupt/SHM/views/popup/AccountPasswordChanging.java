package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.AccountDto;
import com.vupt.SHM.dto.AccountPasswordChangingDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.views.popup.AccountEditing;
import com.vupt.SHM.views.popup.SavingPopupController;

import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AccountPasswordChanging implements SavingPopupController<AccountPasswordChangingDto> {
    @Autowired
    MapstructMapper mapstructMapper;
    @FXML
    TextField tfUsername;
    @FXML
    TextField tfOldPassword;
    @FXML
    TextField tfNewPassword;
    @FXML
    Label lbMessage;
    @FXML
    Button btnSave;
    private AccountDto accountDto;
    private Consumer<AccountPasswordChangingDto> saveHandler;

    public static void loadView(AccountDto accountDTO, Consumer<AccountPasswordChangingDto> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(AccountEditing.class.getResource("/com.vupt.SHM.views/popup/AccountPasswordChange.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));


            com.vupt.SHM.views.popup.AccountPasswordChanging accountPasswordChanging = loader.<com.vupt.SHM.views.popup.AccountPasswordChanging>getController();
            accountPasswordChanging.init(accountDTO, saveHandler);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(AccountDto accountDto, Consumer<AccountPasswordChangingDto> saveHandler) {
        this.saveHandler = saveHandler;
        this.accountDto = accountDto;
        this.tfUsername.setText(accountDto.getUsername());
        this.tfUsername.setEditable(false);
    }


    @FXML
    public void save() {
        if (!checkValidation())
            return;
        try {
            AccountPasswordChangingDto accountPasswordChangingDto = getValueFromForm();
            this.saveHandler.accept(accountPasswordChangingDto);
            close();
        } catch (AppException e) {
            this.lbMessage.setText(e.getMessage());
        }
    }


    @FXML
    public void close() {
        this.btnSave.getScene().getWindow().hide();
    }


    public AccountPasswordChangingDto getValueFromForm() {
        AccountPasswordChangingDto accountPasswordChangingDto = new AccountPasswordChangingDto();
        accountPasswordChangingDto = this.mapstructMapper.accountDtoToAccountPasswordChangingDto(this.accountDto);
        accountPasswordChangingDto.setNewPassword(this.tfNewPassword.getText());
        accountPasswordChangingDto.setOldPassword(this.tfOldPassword.getText());
        return accountPasswordChangingDto;
    }


    public void setValueToForm() {
    }


    public boolean checkValidation() {
        if (this.tfOldPassword.getText().trim().equals("")) {
            this.lbMessage.setText("Mật khẩu cũ không được để trống");
            return false;
        }

        if (this.tfNewPassword.getText().trim().equals("")) {
            this.lbMessage.setText("Mật khẩu mới không được để trống");
            return false;
        }
        return true;
    }
}
