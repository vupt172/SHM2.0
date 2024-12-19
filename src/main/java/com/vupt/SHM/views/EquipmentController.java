package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AuthorityCode;
import com.vupt.SHM.constant.DatetimePattern;
import com.vupt.SHM.constant.EquipmentStatus;
import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.DepartmentSwitchDto;
import com.vupt.SHM.dto.EquipmentDto;
import com.vupt.SHM.dto.EquipmentSavingDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.EquipmentRepository;
import com.vupt.SHM.services.CategoryService;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EmployeeService;
import com.vupt.SHM.services.EquipmentService;
import com.vupt.SHM.utils.AuthenticationUtils;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.utils.DisplayTextUtils;
import com.vupt.SHM.utils.EquipmentDetailMouseClickHandler;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;
import com.vupt.SHM.views.component.EquipmentStatusListCell;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.exception.ErrorDialog;
import com.vupt.SHM.views.popup.EquipmentEditing;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.beans.binding.Bindings;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class EquipmentController implements BaseController<EquipmentSavingDto> {
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    MapstructMapper mapstructMapper;
    @Autowired
    DepartmentConverter departmentConverter;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<EquipmentDto> tbEquipment;
    @FXML
    private TableColumn<EquipmentDto, Long> colId;
    @FXML
    private TableColumn<EquipmentDto, String> colName;
    @FXML
    private TableColumn<EquipmentDto, String> colCode;
    @FXML
    private TableColumn<EquipmentDto, Integer> colYear;
    @FXML
    private TableColumn<EquipmentDto, EquipmentStatus> colStatus;
    @FXML
    private TableColumn<EquipmentDto, Integer> colCount;
    @FXML
    private TableColumn<EquipmentDto, Long> colPrice;
    @FXML
    private TableColumn<EquipmentDto, CategoryDto> colCategory;
    @FXML
    private TableColumn<EquipmentDto, String> colOwner;
    @FXML
    private TableColumn<EquipmentDto, DepartmentDto> colDepartment;
    @FXML
    private TableColumn<EquipmentDto, String> colNote;
    @FXML
    private TableColumn<EquipmentDto, Date> colCreatedDate;
    @FXML
    private TableColumn<EquipmentDto, Date> colUpdatedDate;
    @FXML
    private Label lbCount;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfCode;
    @FXML
    private ComboBox<EquipmentStatus> cbStatus;
    @FXML
    private ComboBox<CategoryDto> cbCategory;
    @FXML
    private ComboBox<DepartmentDto> cbDepartment;
    @FXML
    private Button btnSearch;
    private Stage stage;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.EquipmentController.class.getResource("/com.vupt.SHM.views/Equipment.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Quản lý thiết bị");
            com.vupt.SHM.views.EquipmentController equipmentController = loader.<com.vupt.SHM.views.EquipmentController>getController();
            equipmentController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Quản lý thiết bị");
            return new WindowObject(view, (IWindowController) equipmentController);
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
        initTableView();
        this.cbStatus.setCellFactory(param -> new EquipmentStatusListCell());
        this.cbStatus.setButtonCell((ListCell<EquipmentStatus>) new EquipmentStatusListCell());

        this.cbCategory.setItems(FXCollections.observableList(this.categoryService.findAllDtoIgnoreSuspended()));
        this.cbStatus.getItems().addAll(EquipmentStatus.values());
        this.cbDepartment.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        this.tbEquipment.setOnMouseClicked((EventHandler<? super MouseEvent>) new EquipmentDetailMouseClickHandler(this.tbEquipment));
        this.cbDepartment.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        AutoCompleteBox.build(this.cbDepartment, this.btnSearch);
        this.lbCount.textProperty().bind(Bindings.size(this.tbEquipment.getItems()).asString());
    }


    public void initTableView() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        this.colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colYear.setCellFactory(column -> {
            return new TableCell<EquipmentDto, Integer>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item == -1 ? "" : item.toString()); // Hiển thị tên sản phẩm
                    }
                }
            };
        });

        this.colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setCellFactory(column -> {
            return new TableCell<EquipmentDto, EquipmentStatus>() {
                @Override
                protected void updateItem(EquipmentStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getTitle()); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(column -> {
            return new TableCell<EquipmentDto, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item == -1 ? "" : item.toString()); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCategory.setCellFactory(column -> {
            return new TableCell<EquipmentDto, CategoryDto>() {
                @Override
                protected void updateItem(CategoryDto item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName()); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        this.colDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        colDepartment.setCellFactory(column -> {
            return new TableCell<EquipmentDto, DepartmentDto>() {
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
        this.colCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        colCreatedDate.setCellFactory(column -> {
            return new TableCell<EquipmentDto, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(DateTimeUtils.format(DatetimePattern.DATETIME, item)); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colUpdatedDate.setCellValueFactory(new PropertyValueFactory<>("lastModifiedDate"));
        colUpdatedDate.setCellFactory(column -> {
            return new TableCell<EquipmentDto, Date>() {
                @Override
                protected void updateItem(Date item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(DateTimeUtils.format(DatetimePattern.DATETIME, item)); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
    }


    @FXML
    public void createEntityView() {
        EquipmentEditing.loadView(this::save);
    }


    @FXML
    public void updateEntityView() {
        EquipmentDto selectedEquipmentDTO = this.tbEquipment.getSelectionModel().getSelectedItem();
        if (selectedEquipmentDTO == null)
            return;
        EquipmentSavingDto equipmentSavingDto = this.mapstructMapper.equipmentDtoToEquipmentSavingdto(selectedEquipmentDTO);

        EquipmentEditing.loadView(equipmentSavingDto, this::save);
    }


    @FXML
    public void deleteEntityView() {

        EquipmentDto selectEquipmentDTO = this.tbEquipment.getSelectionModel().getSelectedItem();
        if (selectEquipmentDTO == null) {
            return;
        }

        Optional<ButtonType> result = CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING).setHeaderText(null).setContentText(DisplayTextUtils.getWarningDeleteMessage("Thiết bị", selectEquipmentDTO.getCode()) + "\n[Khi xóa sẽ mất luôn lịch sử thiết bị !]").setYesNoButtonTypes().build().showAndWait();

        if (((ButtonType) result.get()).getButtonData() == ButtonBar.ButtonData.YES) {
            try {
                this.equipmentService.delete(selectEquipmentDTO.getId());
                reload(selectEquipmentDTO.getDepartment());
                CustomNotification.createNotification("Trạng thái", "Xóa thành công", this.tbEquipment).showInformation();
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


    public void save(EquipmentSavingDto equipmentSavingDto) {
        this.equipmentService.save(equipmentSavingDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbEquipment).showInformation();
        reload(equipmentSavingDto.getDepartment());
    }


    public void reload() {
        reload(this.equipmentService.findAll());
    }


    @FXML
    public void refresh() {
        reload();
        clear();
        this.cbCategory.setItems(FXCollections.observableList(this.categoryService.findAllDtoIgnoreSuspended()));
        this.cbDepartment.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
    }


    @FXML
    public void clear() {
        this.tfName.setText("");
        this.tfCode.setText("");
        this.cbStatus.setValue(null);
        this.cbCategory.setValue(null);
        this.cbDepartment.setValue(null);
    }


    @FXML
    public void search() {
        String name = this.tfName.getText();
        String code = this.tfCode.getText();
        long categoryId = (this.cbCategory.getValue() != null) ? ((CategoryDto) this.cbCategory.getValue()).getId() : 0L;
        System.out.println(this.cbDepartment.getValue());
        long departmentId = (this.cbDepartment.getValue() != null) ? ((DepartmentDto) this.cbDepartment.getValue()).getId() : 0L;
        EquipmentStatus status = this.cbStatus.getValue();
        List<EquipmentDto> equipmentDTOList = this.equipmentService.search(name, code, status, categoryId, departmentId);
        reload(equipmentDTOList);
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, (IWindowController) this));
    }


    public void initAppFunctionByRoleAndAuthorities() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        if (!authenticationUtils.hasAuthority(AuthorityCode.CREATE_EQUIPMENT.getAuthority()))
            this.btnCreate.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.UPDATE_EQUIPMENT.getAuthority()))
            this.btnUpdate.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.DELETE_EQUIPMENT.getAuthority()))
            this.btnDelete.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.FIND_EQUIPMENT.getAuthority()))
            this.btnSearch.setDisable(true);
    }

    private void reload(DepartmentDto departmentDto) {
        reload(this.equipmentService.findByDepartmentId(departmentDto.getId()));
    }

    private void reload(List<EquipmentDto> equipmentDTOList) {
        this.tbEquipment.getItems().clear();
        this.tbEquipment.setItems(FXCollections.observableList(equipmentDTOList));
        this.lbCount.textProperty().bind(Bindings.size(this.tbEquipment.getItems()).asString());
    }

    public void switchDepartment(DepartmentSwitchDto departmentSwitchDTO) {
        this.equipmentService.switchDepartment(departmentSwitchDTO);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbEquipment).showInformation();
        reload(departmentSwitchDTO.getNewDepartment());
    }
}
