package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AppConstants;
import com.vupt.SHM.constant.AuthorityCode;
import com.vupt.SHM.dto.AccountDto;
import com.vupt.SHM.dto.AccountPasswordChangingDto;
import com.vupt.SHM.entity.Account;
import com.vupt.SHM.entity.EquipmentRequest;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.CategoryRepository;
import com.vupt.SHM.services.AccountService;
import com.vupt.SHM.utils.AuthenticationUtils;
import com.vupt.SHM.views.AccountController;
import com.vupt.SHM.views.AppAuthorityController;
import com.vupt.SHM.views.CategoryController;
import com.vupt.SHM.views.DepartmentController;
import com.vupt.SHM.views.DepartmentSwitchReportController;
import com.vupt.SHM.views.EmployeeController;
import com.vupt.SHM.views.EquipmentController;
import com.vupt.SHM.views.EquipmentHistoryController;
import com.vupt.SHM.views.LoginController;
import com.vupt.SHM.views.ReportController;
import com.vupt.SHM.views.RoleController;
import com.vupt.SHM.views.StatisticController;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.popup.AccountPasswordChanging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class SHMController {
    @Value("${config.app.info}")
    private String appInfo;
    @Value("${config.app.update}")
    private String appUpdate;
    @Autowired
    AccountService accountService;
    @Autowired
    MapstructMapper mapstructMapper;
    private Stage stage;
    @FXML
    StackPane contentView;
    @FXML
    Menu menuAdmin;
    @FXML
    MenuItem menuItemEmployee;
    @FXML
    MenuItem menuItemRepairer;
    @FXML
    MenuItem menuItemDepartment;
    @FXML
    MenuItem menuItemCategory;
    @FXML
    MenuItem menuItemEquipmentPackage;
    @FXML
    MenuItem menuItemEquipment;
    List<WindowObject> windowObjectList = new ArrayList<>();
    @FXML
    MenuItem menuItemEquipmentRequest;
    @FXML
    MenuItem menuItemEquipmentHistory;
    @FXML
    MenuItem menuItemSwitchDepartment;
    @FXML
    MenuItem menuItemReport;
    @FXML
    MenuItem menuItemStatistic;
    @FXML
    MenuItem menuItemAccount;
    @FXML
    MenuItem menuItemAppAuthority;
    @FXML
    MenuItem menuItemRole;
    @FXML
    MenuItem menuItemAbout;
    @FXML
    Menu menuWelcome;
    @FXML
    MenuItem menuItemLogout;
    @FXML
    MenuItem menuItemChangePassword;
    @Autowired
    CategoryRepository categoryRepository;
    private int totalWindows = 0;
    private IntegerProperty totalWindowsProperty = new SimpleIntegerProperty(0);

    public static void loadView() throws IOException {
        FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.SHMController.class.getResource("/com.vupt.SHM.views/SHM.fxml"));
        loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
        Stage stage = new Stage();
        stage.getIcons().add(new Image(com.vupt.SHM.views.SHMController.class.getResourceAsStream("/images/app_icon.png")));
        Parent view = loader.<Parent>load();
        stage.setScene(new Scene(view));
        stage.setTitle("Hệ thống quản lý thiết bị phần cứng");
        stage.setMaximized(true);
        stage.initStyle(StageStyle.DECORATED);
        com.vupt.SHM.views.SHMController shmController = loader.<com.vupt.SHM.views.SHMController>getController();
        shmController.stage = stage;
        stage.show();
    }


    @FXML
    public void initialize() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        UserDetails userDetails = authenticationUtils.getUserDetails();
        if (userDetails != null) {
            this.menuWelcome.setText("Welcome, " + userDetails.getUsername());
        }
        initMenu();
        initAppFunctionByRoleAndAuthorities();
    }

    private void initMenu() {
        this.menuItemEquipment.setId("Quản lý thiết bị");
        this.menuItemEquipmentRequest.setId(AppConstants.MENU_REQUIPMENT_REQUEST);
        this.menuItemEquipmentHistory.setId("Quản lý lịch sử thiết bị");
        this.menuItemEmployee.setId("Quản lý nhân viên");
        this.menuItemDepartment.setId("Quản lý bộ phận");
        this.menuItemCategory.setId("Quản lý danh mục");
        this.menuItemSwitchDepartment.setId("Chuyển đổi bộ phận thiết bị");
        this.menuItemReport.setId("Hệ thống báo cáo");
        this.menuItemStatistic.setId("Thống kê thiết bị");
        this.menuItemRole.setId("Danh sách vai trò");
        this.menuItemAppAuthority.setId("Danh sách quyền");
        this.menuItemAccount.setId("Quản lý tài khoản");
        this.menuItemEquipmentPackage.setId(AppConstants.MENU_EQUIPMENT_PACKAGE);

    }

    private void initAppFunctionByRoleAndAuthorities() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        if (authenticationUtils.getAuthentication() == null) {
            this.menuItemLogout.setVisible(false);
            return;
        }
        if (!authenticationUtils.hasAuthority(AuthorityCode.ROLE_ADMIN.getAuthority())) {
            this.menuAdmin.setVisible(false);
        }
        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_ACCOUNT.getAuthority()))
            this.menuItemAccount.setVisible(false);

        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_EMPLOYEE.getAuthority()))
            this.menuItemEmployee.setVisible(false);

        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_DEPARTMENT.getAuthority()))
            this.menuItemDepartment.setVisible(false);

        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_CATEGORY.getAuthority()))
            this.menuItemCategory.setVisible(false);

        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_EQUIPMENT.getAuthority()))
            this.menuItemEquipment.setVisible(false);
        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_DEPARTMENT_SWITCH.getAuthority())) {
            this.menuItemSwitchDepartment.setVisible(false);
        }
        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_EQUIPMENT_HISTORY.getAuthority()))
            this.menuItemEquipmentHistory.setVisible(false);
        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_REPORT_SYSTEM.getAuthority()))
            this.menuItemReport.setVisible(false);
        if (!authenticationUtils.hasAuthority(AuthorityCode.VIEW_STATISTIC.getAuthority())) {
            this.menuItemStatistic.setVisible(false);
        }
    }

    @FXML
    public void logout() throws IOException {
        SecurityContextHolder.getContext().setAuthentication(null);
        this.windowObjectList.clear();
        this.contentView.getChildren().clear();
        this.stage.close();
        LoginController.loadView();
    }


    @FXML
    public void manageCategory() throws IOException {
        updateStackPane(this.menuItemCategory, () -> CategoryController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageAccount() throws IOException {
        updateStackPane(this.menuItemAccount, () -> AccountController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageDepartment() throws IOException {
        updateStackPane(this.menuItemDepartment, () -> DepartmentController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageEmployee() throws IOException {
        updateStackPane(this.menuItemEmployee, () -> EmployeeController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageEquipmentPackage() throws IOException {
        updateStackPane(this.menuItemEquipmentPackage, () -> EquipmentPackageController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageEquipment() throws IOException {
        updateStackPane(this.menuItemEquipment, () -> EquipmentController.loadView(this.stage, this::closeView));
    }
    @FXML
    public void manageEquipmentRequest() throws IOException {
        updateStackPane(this.menuItemEquipmentRequest, () -> EquipmentRequestController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void switchDepartment() throws IOException {
        updateStackPane(this.menuItemSwitchDepartment, () -> DepartmentSwitchReportController.loadView(this.stage, this::closeView));
    }


    @FXML
    public void manageEquipmentHistory() throws IOException {
        updateStackPane(this.menuItemEquipmentHistory, () -> EquipmentHistoryController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageReport() throws IOException {
        updateStackPane(this.menuItemReport, () -> ReportController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageStatistic() throws IOException {
        updateStackPane(this.menuItemStatistic, () -> StatisticController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageAppAuthority() throws IOException {
        updateStackPane(this.menuItemAppAuthority, () -> AppAuthorityController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void manageRole() throws IOException {
        updateStackPane(this.menuItemRole, () -> RoleController.loadView(this.stage, this::closeView));
    }

    @FXML
    public void changeAccountPassword() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        String username = authenticationUtils.getUserDetails().getUsername();
        Account curAccount = this.accountService.findByUsername(username);
        AccountDto accountDto = this.mapstructMapper.accountToAccountDto(curAccount);
        AccountPasswordChanging.loadView(accountDto, this::setNewPassword);
    }


    public void setNewPassword(AccountPasswordChangingDto accountPasswordChangingDto) {
        this.accountService.updatePassword(accountPasswordChangingDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.contentView).showInformation();
    }

    public void closeView(WindowObject windowObject) {
        this.contentView.getChildren().remove(windowObject.getView());
        this.windowObjectList.remove(windowObject);
        if (this.windowObjectList.size() > 0) {
            WindowObject lastWindowObject = this.windowObjectList.get(this.windowObjectList.size() - 1);
            this.stage.setTitle(lastWindowObject.getView().getId());
            this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY), () -> lastWindowObject.getController().search());
            this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_ANY), () -> lastWindowObject.getController().closeView());

        } else {
            this.stage.setTitle("SHM");
        }
    }

    private void updateStackPane(MenuItem menuItem, Supplier<WindowObject> windowSupplier) {
        try {
            this.windowObjectList.stream()
                    .forEach(windowObject -> System.out.println(windowObject));
            Optional<WindowObject> optionalWindowObject = this.windowObjectList.stream().filter(windowObject -> windowObject.getView().getId().equals(menuItem.getId())).findFirst();
            if (optionalWindowObject.isPresent()) {
                WindowObject windowObject = optionalWindowObject.get();
                this.contentView.getChildren().remove(windowObject.getView());
                this.contentView.getChildren().add(windowObject.getView());
                this.windowObjectList.remove(windowObject);
                this.windowObjectList.add(windowObject);
                this.stage.setTitle(menuItem.getId());
//                this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F, new KeyCombination.Modifier[]{KeyCombination.CONTROL_ANY}), new Object(this, windowObject));
//                this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, new KeyCombination.Modifier[]{KeyCombination.CONTROL_ANY}), new Object(this, windowObject));


            } else {
                WindowObject windowObject = windowSupplier.get();
                this.windowObjectList.add(windowObject);
                this.contentView.getChildren().add(windowObject.getView());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showApplicationInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, this.appUpdate, new ButtonType[]{ButtonType.OK});
        alert.setTitle("Thông tin ứng dụng");
        alert.setHeaderText(this.appInfo);
        alert.show();
    }
}
