package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AuthorityCode;
import com.vupt.SHM.constant.DepartmentType;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.utils.AuthenticationUtils;
import com.vupt.SHM.utils.DisplayTextUtils;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CellValueFactoryCreator;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.DepartmentTypeListCell;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.exception.ErrorDialog;
import com.vupt.SHM.views.popup.DepartmentEditing;
import com.vupt.SHM.views.popup.DepartmentStructureViewing;

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
public class DepartmentController
        implements BaseController<DepartmentDto> {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    MapstructMapper mapstructMapper;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSearch;
    @FXML
    private TextField tfName;
    @FXML
    private ComboBox<DepartmentType> cbDepartmentType;
    @FXML
    private TableView<DepartmentDto> tbDepartment;
    @FXML
    private TableColumn<DepartmentDto, Long> colId;
    @FXML
    private TableColumn<DepartmentDto, String> colName;
    @FXML
    private TableColumn<DepartmentDto, String> colCode;
    @FXML
    private TableColumn<DepartmentDto, DepartmentType> colType;
    @FXML
    private TableColumn<DepartmentDto, Boolean> colIsSuspended;
    @FXML
    private TableColumn<DepartmentDto, DepartmentDto> colParent;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {

            FXMLLoader loader = new FXMLLoader(DepartmentController.class.getResource("/com.vupt.SHM.views/Department.fxml"));

            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);

            Parent view = loader.<Parent>load();
            view.setId("Quản lý bộ phận");

            DepartmentController departmentController = loader.<com.vupt.SHM.views.DepartmentController>getController();
            departmentController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Quản lý bộ phận");
            return new WindowObject(view, departmentController);

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

        CellValueFactoryCreator.setDepartmentTableViewCellFactory(this.colId, this.colName, this.colCode, this.colType, this.colIsSuspended, this.colParent);
        this.cbDepartmentType.getItems().addAll(DepartmentType.values());
        this.cbDepartmentType.setCellFactory(param -> new DepartmentTypeListCell());
        this.cbDepartmentType.setButtonCell((ListCell<DepartmentType>) new DepartmentTypeListCell());

        this.tbDepartment.setItems(FXCollections.observableList(this.departmentService.findAllDto()));
    }


    public void initTableView() {
    }


    public void createEntityView() {
        DepartmentEditing.loadView(null, this::save);
    }


    public void updateEntityView() {
        DepartmentDto selectedDepartmentDTO = this.tbDepartment.getSelectionModel().getSelectedItem();
        if (selectedDepartmentDTO == null)
            return;
        DepartmentEditing.loadView(selectedDepartmentDTO, this::save);
    }


    public void deleteEntityView() {
        DepartmentDto selectedDepartmentDTO = this.tbDepartment.getSelectionModel().getSelectedItem();

        if (selectedDepartmentDTO == null) {
            return;
        }


        Optional<ButtonType> result = CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING).setHeaderText(null).setContentText(DisplayTextUtils.getWarningDeleteMessage("Bộ phận", selectedDepartmentDTO.getName())).setYesNoButtonTypes().build().showAndWait();

        if (((ButtonType) result.get()).getButtonData() == ButtonBar.ButtonData.YES) {
            try {
                this.departmentService.delete(selectedDepartmentDTO.getId());
                reload();

                CustomNotification.createNotification("Trạng thái", "Xóa thành công", this.tbDepartment).showInformation();
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


    public void save(DepartmentDto departmentDto) {
        this.departmentService.save(departmentDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbDepartment).showInformation();
        reload();
    }


    @FXML
    public void reload() {
        reload(this.departmentService.findAllDto());
    }


    @FXML
    public void refresh() {
        reload();
        clear();
    }


    @FXML
    public void clear() {
        this.tfName.setText("");
        this.cbDepartmentType.setValue(null);
    }


    @FXML
    public void search() {
        String departmentName = this.tfName.getText();
        DepartmentType departmentType = this.cbDepartmentType.getValue();
        List<DepartmentDto> departmentDTOList = this.departmentService.search(departmentName, departmentType);
        reload(departmentDTOList);
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, (IWindowController) this));
    }

    @FXML
    private void viewDepartmentStructure() {
        DepartmentStructureViewing.loadView();
    }

    public void initAppFunctionByRoleAndAuthorities() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        if (!authenticationUtils.hasAuthority(AuthorityCode.CREATE_DEPARTMENT.getAuthority()))
            this.btnCreate.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.UPDATE_DEPARTMENT.getAuthority()))
            this.btnUpdate.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.DELETE_DEPARTMENT.getAuthority()))
            this.btnDelete.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.FIND_DEPARTMENT.getAuthority()))
            this.btnSearch.setDisable(true);
    }

    private void reload(List<DepartmentDto> departmentDTOList) {
        this.tbDepartment.getItems().clear();
        this.tbDepartment.setItems(FXCollections.observableList(departmentDTOList));
    }
}


