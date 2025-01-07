package com.vupt.SHM.views;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AppConstants;
import com.vupt.SHM.constant.DatetimePattern;
import com.vupt.SHM.dto.*;
import com.vupt.SHM.entity.Equipment;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EquipmentRequestService;
import com.vupt.SHM.services.EquipmentService;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.utils.DisplayTextUtils;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.exception.ErrorDialog;
import com.vupt.SHM.views.popup.EquipmentRequestSaving;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


@Controller
public class EquipmentRequestController implements BaseController<EquipmentRequestSavingDto> {
    @Autowired
    EquipmentRequestService equipmentRequestService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentConverter departmentConverter;
    @FXML
    TableView<EquipmentRequestDto> tbEquipmentRequest;
    @FXML
    TableColumn<EquipmentRequestDto, Long> colId;
    @FXML
    TableColumn<EquipmentRequestDto, String> colName;
    @FXML
    TableColumn<EquipmentRequestDto, String> colStatus;
    @FXML
    TableColumn<EquipmentRequestDto, String> colSolution;
    @FXML
    TableColumn<EquipmentRequestDto, String> colEmployeeRequest;
    @FXML
    TableColumn<EquipmentRequestDto, DepartmentDto> colDepartment;
    @FXML
    TableColumn<EquipmentRequestDto, Date> colDate;
    @FXML
    TableColumn<EquipmentRequestDto, String> colResult;
    @FXML
    TableColumn<EquipmentRequestDto, String> colNote;
    @FXML
    TableColumn<EquipmentRequestDto, Boolean> colIsDone;

    @FXML
    ComboBox<DepartmentDto> cbDepartment;
    @FXML
    DatePicker dpStartDate;
    @FXML
    DatePicker dpEndDate;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(EquipmentRequestController.class.getResource("/com.vupt.SHM.views/EquipmentRequest.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.load();
            view.setId(AppConstants.MANAGE_EQUIPMENT_REQUEST);
            EquipmentRequestController equipmentRequestController = loader.getController();
            equipmentRequestController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle(AppConstants.MANAGE_EQUIPMENT_REQUEST);
            return new WindowObject(view, equipmentRequestController);
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

    @Override
    @FXML
    public void initialize() {
        initTableView();
        tbEquipmentRequest.getItems().addAll(FXCollections.observableArrayList(equipmentRequestService.findAllDto()));
        cbDepartment.getItems().addAll(FXCollections.observableArrayList(departmentService.findAllDtoIgnoreSuspended()));
        AutoCompleteBox.build(cbDepartment, departmentConverter);
    }

    @Override
    public void initTableView() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.colSolution.setCellValueFactory(new PropertyValueFactory<>("solution"));
        this.colResult.setCellValueFactory(new PropertyValueFactory<>("result"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setCellFactory(column -> {
            return new TableCell<EquipmentRequestDto, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(DateTimeUtils.format(DatetimePattern.DATE, item)); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colEmployeeRequest.setCellValueFactory(new PropertyValueFactory<>("employeeRequest"));
        this.colDepartment.setCellValueFactory(new PropertyValueFactory<>("departmentDto"));
        colDepartment.setCellFactory(column -> {
            return new TableCell<EquipmentRequestDto, DepartmentDto>() {
                @Override
                protected void updateItem(DepartmentDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName()); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
        this.colIsDone.setCellValueFactory(param -> new SimpleBooleanProperty(((EquipmentRequestDto) param.getValue()).isDone()));
        this.colIsDone.setCellFactory(CheckBoxTableCell.forTableColumn(this.colIsDone));
    }

    @Override
    @FXML
    public void createEntityView() {
        EquipmentRequestSaving.loadView(this::save);
    }

    @Override
    @FXML
    public void updateEntityView() {
        EquipmentRequestDto equipmentRequestDto = tbEquipmentRequest.getSelectionModel().getSelectedItem();
        if (equipmentRequestDto == null) return;
        if (equipmentRequestDto.isDone()) {
            CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING)
                    .setHeaderText(null)
                    .setTitle("Thông báo hệ thống")
                    .setContentText("Giấy đề nghị đã được duyệt !")
                    .build().show();
            return;
        }
        EquipmentRequestSaving.loadView(equipmentRequestDto, this::save);
    }

    @Override
    @FXML
    public void deleteEntityView() {
        EquipmentRequestDto equipmentRequestDto = this.tbEquipmentRequest.getSelectionModel().getSelectedItem();
        if (equipmentRequestDto == null) return;

        Optional<ButtonType> result = CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING)
                .setHeaderText(null)
                .setContentText(DisplayTextUtils.getWarningDeleteMessage(AppConstants.MENU_REQUIPMENT_REQUEST, String.valueOf(equipmentRequestDto.getId())))
                .setYesNoButtonTypes()
                .build().showAndWait();

        if ((result.get()).getButtonData() == ButtonBar.ButtonData.YES) {
            try {
                this.equipmentRequestService.delete(equipmentRequestDto.getId());
                reload();
                CustomNotification.createNotification("Trạng thái", "Xóa thành công", this.tbEquipmentRequest).showInformation();
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

    @Override
    public void save(EquipmentRequestSavingDto equipmentRequestSavingDto) {
        this.equipmentRequestService.save(equipmentRequestSavingDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbEquipmentRequest).showInformation();
        reload();
    }

    @Override
    public void reload() {
        reload(equipmentRequestService.findAllDto());
    }

    private void reload(List<EquipmentRequestDto> equipmentRequestDtoList) {
        this.tbEquipmentRequest.getItems().clear();
        this.tbEquipmentRequest.getItems().addAll(equipmentRequestDtoList);
    }

    private void reload(DepartmentDto departmentDto) {
        reload(equipmentRequestService.findByDepartmentId(departmentDto.getId()));
    }

    @Override
    @FXML
    public void refresh() {
        clear();
        reload();
    }

    @Override
    public void clear() {
        cbDepartment.setValue(null);
    }

    @Override
    @FXML
    public void search() {
        DepartmentDto departmentDto = cbDepartment.getValue();
        long departmentId = departmentDto == null ? 0 : departmentDto.getId();
        Date startDate =dpStartDate.getValue()==null?null: DateTimeUtils.asDate(dpStartDate.getValue());
        Date endDate = dpEndDate.getValue()==null?null:DateTimeUtils.asDate(dpEndDate.getValue());
        List<EquipmentRequestDto> equipmentRequestDtoList = equipmentRequestService.filterSearch(departmentId, startDate, endDate);
        reload(equipmentRequestDtoList);
    }

    @Override
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, this));
    }

    @FXML
    public void approveRequest() {
        EquipmentRequestDto equipmentRequestDto = tbEquipmentRequest.getSelectionModel().getSelectedItem();
        if (equipmentRequestDto == null) return;
        equipmentRequestService.approveRequest(equipmentRequestDto.getId());
        reload();
    }

    @FXML
    public void unapproveRequest() {
        EquipmentRequestDto equipmentRequestDto = tbEquipmentRequest.getSelectionModel().getSelectedItem();
        if (equipmentRequestDto == null) return;
        equipmentRequestService.unapproveRequest(equipmentRequestDto.getId());
        reload();
    }
}
