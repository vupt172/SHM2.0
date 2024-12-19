package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AuthorityCode;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.EmployeeDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EmployeeService;
import com.vupt.SHM.utils.AuthenticationUtils;
import com.vupt.SHM.utils.DisplayTextUtils;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CellValueFactoryCreator;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.exception.ErrorDialog;
import com.vupt.SHM.views.popup.EmployeeEditing;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EmployeeController implements BaseController<EmployeeDto> {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    DepartmentService departmentService;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<EmployeeDto> tbEmployee;
    @FXML
    private TableColumn<EmployeeDto, Long> colId;
    @FXML
    private TableColumn<EmployeeDto, String> colName;
    @FXML
    private TableColumn<EmployeeDto, String> colUsername;
    @FXML
    private TableColumn<EmployeeDto, DepartmentDto> colDepartment;
    @FXML
    private TableColumn<EmployeeDto, String> colContact;
    @FXML
    private TableColumn<EmployeeDto, Boolean> colIsManager;
    @FXML
    private TextField tfName;
    @FXML
    private ComboBox<DepartmentDto> cbDepartment;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.EmployeeController.class.getResource("/com.vupt.SHM.views/Employee.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Quản lý nhân viên");
            com.vupt.SHM.views.EmployeeController employeeController = loader.<com.vupt.SHM.views.EmployeeController>getController();
            employeeController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Quản lý nhân viên");
            return new WindowObject(view, (IWindowController) employeeController);
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


    @FXML
    public void initialize() {
        initAppFunctionByRoleAndAuthorities();
        initTableView();
        this.tbEmployee.setItems(FXCollections.observableList(this.employeeService.findALl()));
        this.cbDepartment.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
    }


    public void initTableView() {
        CellValueFactoryCreator.setEmployeeTableViewCellFactory(this.colId, this.colName, this.colUsername, this.colDepartment, this.colContact, this.colIsManager);
    }


    @FXML
    public void createEntityView() {
        EmployeeEditing.loadView(this::save, this.tbEmployee);
    }


    @FXML
    public void updateEntityView() {
        EmployeeDto selectedEmployeeDTO = this.tbEmployee.getSelectionModel().getSelectedItem();
        if (selectedEmployeeDTO == null)
            return;
        EmployeeEditing.loadView(selectedEmployeeDTO, this::save, this.tbEmployee);
    }


    @FXML
    public void deleteEntityView() {
        EmployeeDto selectedEmployeeDTO = this.tbEmployee.getSelectionModel().getSelectedItem();
        if (selectedEmployeeDTO == null) {
            return;
        }


        Optional<ButtonType> result = CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING).setHeaderText(null).setContentText(DisplayTextUtils.getWarningDeleteMessage("Nhân viên", selectedEmployeeDTO.getFullName())).setYesNoButtonTypes().build().showAndWait();

        if (((ButtonType) result.get()).getButtonData() == ButtonBar.ButtonData.YES) {
            try {
                this.employeeService.delete(selectedEmployeeDTO.getId());
                reload();
                CustomNotification.createNotification("Trạng thái", "Xóa thành công", this.tbEmployee).showInformation();
            } catch (AppException e) {
                CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING)
                        .setHeaderText(null)
                        .setContentText(e.getMessage())
                        .build()
                        .show();
            } catch (SQLException e) {
                ErrorDialog.showErrorDialog((Throwable) e);
            }
        }
    }


    public void save(EmployeeDto employeeDto) {
        this.employeeService.save(employeeDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbEmployee).showInformation();
        reload();
    }


    public void reload() {
        reload(this.employeeService.findALl());
    }


    @FXML
    public void refresh() {
        reload();
        clear();
    }


    @FXML
    public void clear() {
        this.tfName.setText("");
        this.cbDepartment.setValue(null);
    }


    @FXML
    public void search() {
        String employeeName = this.tfName.getText();
        long departmentId = (this.cbDepartment.getValue() != null) ? ((DepartmentDto) this.cbDepartment.getValue()).getId() : 0L;
        List<EmployeeDto> employeeDtoList = this.employeeService.search(employeeName, departmentId);
        reload(employeeDtoList);
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, (IWindowController) this));
    }


    public void initAppFunctionByRoleAndAuthorities() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        if (!authenticationUtils.hasAuthority(AuthorityCode.CREATE_EMPLOYEE.getAuthority()))
            this.btnCreate.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.UPDATE_EMPLOYEE.getAuthority()))
            this.btnUpdate.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.DELETE_EMPLOYEE.getAuthority()))
            this.btnDelete.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.FIND_EMPLOYEE.getAuthority()))
            this.btnSearch.setDisable(true);
    }

    private void reload(List<EmployeeDto> employeeDtoList) {
        this.tbEmployee.getItems().clear();
        this.tbEmployee.setItems(FXCollections.observableList(employeeDtoList));
    }
}


