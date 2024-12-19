package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AuthorityCode;
import com.vupt.SHM.constant.DatetimePattern;
import com.vupt.SHM.constant.DepartmentSwitchReason;
import com.vupt.SHM.dto.*;
import com.vupt.SHM.entity.DepartmentSwitchReport;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.reports.DepartmentSwitchReportExporter;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.DepartmentSwitchReportFileService;
import com.vupt.SHM.services.DepartmentSwitchReportService;
import com.vupt.SHM.utils.AuthenticationUtils;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.utils.DisplayTextUtils;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.DepartmentController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.exception.ErrorDialog;
import com.vupt.SHM.views.popup.DepartmentSwitchReportAttaching;
import com.vupt.SHM.views.popup.DepartmentSwitchReportViewing;
import com.vupt.SHM.views.popup.DepartmentSwitchSaving;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class DepartmentSwitchReportController implements BaseController<DepartmentSwitchReportSavingDto> {
    @Autowired
    DepartmentSwitchReportService departmentSwitchReportService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentConverter departmentConverter;
    @Autowired
    DepartmentSwitchReportFileService departmentSwitchReportFileService;
    @Autowired
    MapstructMapper mapstructMapper;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    TableView<DepartmentSwitchReportDto> tbDepartmentSwitch;
    @FXML
    TableColumn<DepartmentSwitchReportDto, Long> colId;
    @FXML
    TableColumn<DepartmentSwitchReportDto, DepartmentDto> colDepartmentFrom;
    @FXML
    TableColumn<DepartmentSwitchReportDto, DepartmentDto> colDepartmentTo;
    @FXML
    TableColumn<DepartmentSwitchReportDto, Date> colSwitchDate;
    @FXML
    TableColumn<DepartmentSwitchReportDto, DepartmentSwitchReason> colSwitchReason;
    @FXML
    TableColumn<DepartmentSwitchReportDto, String> colMessage;
    @FXML
    TableColumn<DepartmentSwitchReportDto, Boolean> colIsFlush;
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
            FXMLLoader loader = new FXMLLoader(DepartmentController.class.getResource("/com.vupt.SHM.views/DepartmentSwitchReport.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Chuyển đổi bộ phận thiết bị");

            com.vupt.SHM.views.DepartmentSwitchReportController departmentSwitchController = loader.getController();
            departmentSwitchController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Chuyển đổi bộ phận thiết bị");
            return new WindowObject(view, (IWindowController) departmentSwitchController);
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
        if (!authenticationUtils.hasAuthority(AuthorityCode.CREATE_DEPARTMENT_SWITCH.getAuthority()))
            this.btnCreate.setDisable(true);

        if (!authenticationUtils.hasAuthority(AuthorityCode.UPDATE_DEPARTMENT_SWITCH.getAuthority()))
            this.btnUpdate.setDisable(true);

        if (!authenticationUtils.hasAuthority(AuthorityCode.DELETE_DEPARTMENT_SWITCH.getAuthority()))
            this.btnDelete.setDisable(true);

        if (!authenticationUtils.hasAuthority(AuthorityCode.FIND_DEPARTMENT_SWITCH.getAuthority())) {

            this.btnSearch.setDisable(true);
        }
    }

    @FXML
    public void initialize() {
        initAppFunctionByRoleAndAuthorities();
        initTableView();
        this.cbDepartmentFrom.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        this.cbDepartmentFrom.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        AutoCompleteBox.build(this.cbDepartmentFrom, this.cbDepartmentTo);
        this.cbDepartmentTo.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        this.cbDepartmentTo.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        AutoCompleteBox.build(this.cbDepartmentTo, this.btnSearch);
    }


    public void initTableView() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colDepartmentFrom.setCellValueFactory(new PropertyValueFactory<>("departmentFrom"));
        colDepartmentFrom.setCellValueFactory(new PropertyValueFactory<>("departmentFrom"));
        colDepartmentFrom.setCellFactory(column -> {
            return new TableCell<DepartmentSwitchReportDto, DepartmentDto>() {
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

        this.colDepartmentTo.setCellValueFactory(new PropertyValueFactory<>("departmentTo"));
        colDepartmentTo.setCellFactory(column -> {
            return new TableCell<DepartmentSwitchReportDto, DepartmentDto>() {
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
        this.colSwitchDate.setCellValueFactory(new PropertyValueFactory<>("switchDate"));
        colSwitchDate.setCellFactory(column -> {
            return new TableCell<DepartmentSwitchReportDto, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(DateTimeUtils.format(DatetimePattern.DATETIME,item)); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colSwitchReason.setCellValueFactory(new PropertyValueFactory<>("departmentSwitchReason"));
        colSwitchReason.setCellFactory(column -> {
            return new TableCell<DepartmentSwitchReportDto, DepartmentSwitchReason>() {
                @Override
                protected void updateItem(DepartmentSwitchReason item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getReason()); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        this.tbDepartmentSwitch.setItems(FXCollections.observableList(this.departmentSwitchReportService.findAll()));
        this.tbDepartmentSwitch.setOnMouseClicked(event -> {
            DepartmentSwitchReportDto departmentSwitchReportDto=tbDepartmentSwitch.getSelectionModel().getSelectedItem();
            if(departmentSwitchReportDto==null)return;
            DepartmentSwitchReportExDto departmentSwitchReportExDto= departmentSwitchReportService.getExDto(departmentSwitchReportDto.getId());
            DepartmentSwitchReportViewing.loadView(departmentSwitchReportExDto);
        });
        this.colIsFlush.setCellValueFactory(param -> new SimpleBooleanProperty(((DepartmentSwitchReportDto) param.getValue()).isFlush()));
        this.colIsFlush.setCellFactory(CheckBoxTableCell.forTableColumn(this.colIsFlush));
    }


    @FXML
    public void createEntityView() {
        DepartmentSwitchSaving.loadView(this::save);
    }


    @FXML
    public void updateEntityView() {
        DepartmentSwitchReportDto departmentSwitchReportDto = this.tbDepartmentSwitch.getSelectionModel().getSelectedItem();
        if (departmentSwitchReportDto == null)
            return;
        if (departmentSwitchReportDto.isFlush()) {

            CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING)
                    .setHeaderText(null)
                    .setContentText("Biên bản này đã duyệt không thể cập nhật!")
                    .build()
                    .show();
            return;
        }

        DepartmentSwitchSaving.loadView(this.departmentSwitchReportService.getSavingDto(departmentSwitchReportDto.getId()), this::save);
    }


    public void deleteEntityView() {
        DepartmentSwitchReportDto departmentSwitchReportDto = this.tbDepartmentSwitch.getSelectionModel().getSelectedItem();
        if (departmentSwitchReportDto == null) {
            return;
        }
        Optional<ButtonType> result = CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING).setHeaderText(null).setContentText(DisplayTextUtils.getWarningDeleteMessage("Phiếu chuyển đổi bộ phận", String.valueOf(departmentSwitchReportDto.getId()))).setYesNoButtonTypes().build().showAndWait();

        if (((ButtonType) result.get()).getButtonData() == ButtonBar.ButtonData.YES) {
            try {
                this.departmentSwitchReportService.delete(departmentSwitchReportDto.getId());
                reload();
                CustomNotification.createNotification("Trạng thái", "Xóa thành công", this.tbDepartmentSwitch).showInformation();
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


    public void save(DepartmentSwitchReportSavingDto departmentSwitchReportSavingDto) {
        this.departmentSwitchReportService.save(departmentSwitchReportSavingDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbDepartmentSwitch).showInformation();
        reload();
    }


    @FXML
    public void search() {
        long departmentFromId = (this.cbDepartmentFrom.getValue() != null) ? ((DepartmentDto) this.cbDepartmentFrom.getValue()).getId() : 0L;
        long departmentToId = (this.cbDepartmentTo.getValue() != null) ? ((DepartmentDto) this.cbDepartmentTo.getValue()).getId() : 0L;
        List<DepartmentSwitchReportDto> departmentSwitchReportDtoList = this.departmentSwitchReportService.search(departmentFromId, departmentToId);
        reload(departmentSwitchReportDtoList);
    }


    public void reload() {
        reload(this.departmentSwitchReportService.findAll());
    }


    @FXML
    public void refresh() {
        reload();
        clear();
    }


    @FXML
    public void clear() {
        this.cbDepartmentTo.setValue(null);
        this.cbDepartmentFrom.setValue(null);
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, (IWindowController) this));
    }


    @FXML
    public void flushReport() {
        DepartmentSwitchReportDto departmentSwitchReportDto = this.tbDepartmentSwitch.getSelectionModel().getSelectedItem();
        if (departmentSwitchReportDto != null) {
            if (departmentSwitchReportDto.isFlush()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Biên bản này đã được duyệt", new ButtonType[]{ButtonType.OK});
                alert.setHeaderText(null);
                alert.show();
                return;
            }
            this.departmentSwitchReportService.flushReport(departmentSwitchReportDto.getId());

            CustomNotification.createNotification("Trạng thái", "Duyệt thành công", this.tbDepartmentSwitch).showInformation();
            reload();
        }
    }

    @FXML
    public void exportReport() {
        DepartmentSwitchReportDto departmentSwitchReportDto = this.tbDepartmentSwitch.getSelectionModel().getSelectedItem();
        if (departmentSwitchReportDto == null)
            return;
        DepartmentSwitchReport departmentSwitchReport = this.departmentSwitchReportService.findDepartmentSwitchReportWithDetails(departmentSwitchReportDto.getId());
        DepartmentSwitchReportExporter.exportReport(departmentSwitchReport);
    }

    @FXML
    public void attachFiles() {
        DepartmentSwitchReportDto departmentSwitchReportDto = this.tbDepartmentSwitch.getSelectionModel().getSelectedItem();
        if (departmentSwitchReportDto == null)
            return;
        if (!departmentSwitchReportDto.isFlush()) {
            CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING)
                    .setHeaderText(null)
                    .setContentText("Biên bản này chưa được duyệt không thể đính kèm file!")
                    .build()
                    .show();
            return;
        }
        DepartmentSwitchReport departmentSwitchReport = this.departmentSwitchReportService.findDepartmentSwitchReportWithFiles(departmentSwitchReportDto.getId());
        DepartmentSwitchReportAttachingDto departmentSwitchReportAttachingDto = this.mapstructMapper.departmentSwitchReportToDepartmentSwitchReportAttachingDto(departmentSwitchReport);
        DepartmentSwitchReportAttaching.loadView(departmentSwitchReportAttachingDto, this::saveAttachFiles);
    }


    @FXML
    public void saveAttachFiles(long departmentSwitchReportId, List<DepartmentSwitchReportFileDto> departmentSwitchReportFileDtoList) {
        this.departmentSwitchReportFileService.saveAll(departmentSwitchReportId, departmentSwitchReportFileDtoList);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbDepartmentSwitch).showInformation();
        reload();
    }

    private void reload(List<DepartmentSwitchReportDto> departmentSwitchReportDtoList) {
        this.tbDepartmentSwitch.getItems().clear();
        this.tbDepartmentSwitch.setItems(FXCollections.observableList(departmentSwitchReportDtoList));
    }
}
