package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AuthorityCode;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.EquipmentHistory;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EquipmentHistoryService;
import com.vupt.SHM.utils.AuthenticationUtils;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CellValueFactoryCreator;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;
import com.vupt.SHM.views.component.WindowObject;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class EquipmentHistoryController
        implements IWindowController {
    @Autowired
    DepartmentConverter departmentConverter;
    @Autowired
    EquipmentHistoryService equipmentHistoryService;
    @Autowired
    DepartmentService departmentService;
    @FXML
    TableView<EquipmentHistory> tbEquipmentHistory;
    @FXML
    TableColumn<EquipmentHistory, Long> colId;
    @FXML
    TableColumn<EquipmentHistory, String> colName;
    @FXML
    TableColumn<EquipmentHistory, String> colCode;
    @FXML
    TableColumn<EquipmentHistory, Department> colDepartmentOld;
    @FXML
    TableColumn<EquipmentHistory, Department> colDepartmentNew;
    @FXML
    TableColumn<EquipmentHistory, Date> colTransportDate;
    @FXML
    TableColumn<EquipmentHistory, String> colMessage;
    @FXML
    TextField tfEquipmentCode;
    @FXML
    ComboBox<DepartmentDto> cbDepartmentFrom;
    @FXML
    ComboBox<DepartmentDto> cbDepartmentTo;
    @FXML
    Button btnSearch;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.EquipmentHistoryController.class.getResource("/com.vupt.SHM.views/EquipmentHistory.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Quản lý lịch sử thiết bị");
            com.vupt.SHM.views.EquipmentHistoryController equipmentHistoryController = loader.<com.vupt.SHM.views.EquipmentHistoryController>getController();
            equipmentHistoryController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Quản lý lịch sử thiết bị");
            return new WindowObject(view, equipmentHistoryController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void init(Stage primaryStage, Consumer<WindowObject> closeViewHandler, Parent view) {
        this.stage = primaryStage;
        this.closeViewHandler = closeViewHandler;
        this.view = view;
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY), () -> search());
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_ANY), () -> closeView());
    }


    @FXML
    public void initialize() {
        initAppFunctionByRoleAndAuthorities();
        CellValueFactoryCreator.setEquipmentHistoryTableViewCellFactory(this.colId, this.colName, this.colCode, this.colDepartmentOld, this.colDepartmentNew, this.colTransportDate, this.colMessage);
        this.cbDepartmentFrom.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        this.cbDepartmentTo.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        this.cbDepartmentFrom.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        this.cbDepartmentTo.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        AutoCompleteBox.build(this.cbDepartmentFrom, this.cbDepartmentTo);
        AutoCompleteBox.build(this.cbDepartmentTo, this.btnSearch);
    }


    public void initAppFunctionByRoleAndAuthorities() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        if (!authenticationUtils.hasAuthority(AuthorityCode.FIND_EQUIPMENT_HISTORY.getAuthority()))
            this.btnSearch.setDisable(true);
    }

    public void reload(List<EquipmentHistory> equipmentHistoryList) {
        this.tbEquipmentHistory.getItems().clear();
        this.tbEquipmentHistory.setItems(FXCollections.observableList(equipmentHistoryList));
    }

    public void reload() {
        reload(this.equipmentHistoryService.findAll());
    }

    @FXML
    public void search() {
        String code = this.tfEquipmentCode.getText();
        long departmentFromId = (this.cbDepartmentFrom.getValue() != null) ? ((DepartmentDto) this.cbDepartmentFrom.getValue()).getId() : 0L;
        long departmentToId = (this.cbDepartmentTo.getValue() != null) ? ((DepartmentDto) this.cbDepartmentTo.getValue()).getId() : 0L;
        List<EquipmentHistory> equipmentHistoryList = this.equipmentHistoryService.search(code, departmentFromId, departmentToId);
        reload(equipmentHistoryList);
    }

    @FXML
    public void clear() {
        this.tfEquipmentCode.setText("");
        this.cbDepartmentFrom.setValue(null);
        this.cbDepartmentTo.setValue(null);
    }

    @FXML
    public void refresh() {
        reload();
        clear();
    }

    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, this));
    }
}
