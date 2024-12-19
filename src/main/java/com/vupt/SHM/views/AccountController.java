package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AuthorityCode;
import com.vupt.SHM.dto.AccountDto;
import com.vupt.SHM.dto.AccountExDto;
import com.vupt.SHM.dto.AccountPasswordChangingDto;
import com.vupt.SHM.entity.Account;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.services.AccountService;
import com.vupt.SHM.utils.AuthenticationUtils;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.popup.AccountEditing;
import com.vupt.SHM.views.popup.AccountRolesAdjusting;

import java.util.List;
import java.util.function.Consumer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class AccountController implements BaseController<AccountDto> {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MapstructMapper mapstructMapper;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSearch;
    @FXML
    TableView<AccountDto> tbAccount;

    @FXML
    private TableColumn<AccountDto, Long> colId;
    @FXML
    private TableColumn<AccountDto, String> colUsername;
    @FXML
    private TableColumn<AccountDto, String> colFullName;
    @FXML
    private TableColumn<AccountDto, Boolean> colIsSuspended;
    @FXML
    TextField tfHoTen;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.AccountController.class.getResource("/com.vupt.SHM.views/Account.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Quản lý tài khoản");

            AccountController accountController = loader.getController();
            accountController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Quản lý tài khoản");
            return new WindowObject(view, (IWindowController) accountController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void init(Stage primaryStage, Consumer<WindowObject> closeViewHandler, Parent view) {
        this.stage = primaryStage;
        this.closeViewHandler = closeViewHandler;
        this.view = view;
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY), ()->search());
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_ANY), ()->closeView());
    }


    public void initAppFunctionByRoleAndAuthorities() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        if (!authenticationUtils.hasAuthority(AuthorityCode.CREATE_ACCOUNT.getAuthority()))
            this.btnCreate.setDisable(true);

        if (!authenticationUtils.hasAuthority(AuthorityCode.UPDATE_ACCOUNT.getAuthority()))
            this.btnUpdate.setDisable(true);

        if (!authenticationUtils.hasAuthority(AuthorityCode.DELETE_ACCOUNT.getAuthority()))
            this.btnDelete.setDisable(true);

        if (!authenticationUtils.hasAuthority(AuthorityCode.FIND_ACCOUNT.getAuthority()))
            this.btnSearch.setDisable(true);
    }

    private ContextMenu initContextMenu(TableView<AccountDto> tbAccount) {

        ContextMenu contextMenu = new ContextMenu();

        MenuItem itemResetPassword = new MenuItem("Reset mật khẩu");

        MenuItem itemSetAccounRoles = new MenuItem("Phân quyền tài khoản");

        itemResetPassword.setOnAction(e -> {
            AccountDto selectedAccount = tbAccount.getSelectionModel().getSelectedItem();


            if (selectedAccount != null) {
                this.accountService.resetPassword(selectedAccount);

                CustomAlert.AlertBuilder.builder(Alert.AlertType.INFORMATION).setHeaderText(null).setContentText("Reset mật khẩu thành công \nMật khẩu mới : 123").setTitle("Reset password").build().show();
            }
        });

        itemSetAccounRoles.setOnAction(e -> {
            AccountDto selectedAccount = tbAccount.getSelectionModel().getSelectedItem();
            if (selectedAccount != null) {
                Account curAccount = this.accountService.findAccountWithRolesById(selectedAccount.getId());
                AccountExDto accountExDto = this.mapstructMapper.accountToAccountExDto(curAccount);
                AccountRolesAdjusting.loadView(accountExDto, this::adjustRoles);
            }
        });
        contextMenu.getItems().addAll(new MenuItem[]{itemResetPassword, itemSetAccounRoles});
        return contextMenu;
    }

    public void setNewPassword(AccountPasswordChangingDto accountPasswordChangingDto) {
        this.accountService.updatePassword(accountPasswordChangingDto);

        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbAccount).showInformation();
        reload();
    }

    private void adjustRoles(AccountExDto accountExDto) {
        this.accountService.updateRoles(accountExDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbAccount).showInformation();
        reload();
    }


    @FXML
    public void initialize() {
        initAppFunctionByRoleAndAuthorities();
        initTableView();

        this.tbAccount.setContextMenu(initContextMenu(this.tbAccount));
        this.tbAccount.setItems(FXCollections.observableList(this.accountService.findAll()));
    }


    public void initTableView() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        this.colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        this.colIsSuspended.setCellValueFactory(param -> new SimpleBooleanProperty(((AccountDto) param.getValue()).isSuspended()));
        this.colIsSuspended.setCellFactory(CheckBoxTableCell.forTableColumn(this.colIsSuspended));
    }


    public void createEntityView() {
        AccountEditing.loadView(this::save);
    }


    public void updateEntityView() {
        AccountDto accountDto = this.tbAccount.getSelectionModel().getSelectedItem();
        if (accountDto == null)
            return;
        AccountEditing.loadView(accountDto, this::save);
    }


    public void deleteEntityView() {
    }


    public void save(AccountDto accountDto) {
        this.accountService.save(accountDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbAccount).showInformation();
        reload();
    }


    @FXML
    public void search() {
        String hoTen = this.tfHoTen.getText();
        List<AccountDto> accountDtoList = this.accountService.search(hoTen);
        reload(accountDtoList);
    }


    public void reload() {
        reload(this.accountService.findAll());
    }

    private void reload(List<AccountDto> accountDtoList) {
        this.tbAccount.getItems().clear();
        this.tbAccount.setItems(FXCollections.observableList(accountDtoList));
    }


    @FXML
    public void refresh() {
        reload();
        clear();
    }


    @FXML
    public void clear() {
        this.tfHoTen.setText("");
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, (IWindowController) this));
    }
}
