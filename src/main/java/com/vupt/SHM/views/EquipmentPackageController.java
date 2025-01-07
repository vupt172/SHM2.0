package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AppConstants;
import com.vupt.SHM.constant.DatetimePattern;
import com.vupt.SHM.dto.EquipmentDto;
import com.vupt.SHM.dto.EquipmentPackageDto;
import com.vupt.SHM.dto.EquipmentRequestDto;
import com.vupt.SHM.entity.EquipmentPackage;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.services.EquipmentPackageService;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.utils.DisplayTextUtils;
import com.vupt.SHM.utils.FormatUtils;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.exception.ErrorDialog;
import com.vupt.SHM.views.popup.EquipmentPackageSaving;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
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
public class EquipmentPackageController implements BaseController<EquipmentPackageDto> {
    @Autowired
    EquipmentPackageService equipmentPackageService;
    @FXML
    TableView<EquipmentPackageDto> tbEquipmentPackage;
    @FXML
    TableColumn<EquipmentPackageDto, Long> colId;
    @FXML
    TableColumn<EquipmentPackageDto, String> colName;
    @FXML
    TableColumn<EquipmentPackageDto, String> colDetail;
    @FXML
    TableColumn<EquipmentPackageDto, Long> colPrice;
    @FXML
    TableColumn<EquipmentPackageDto, Date> colDate;
    @FXML
    TableColumn<EquipmentPackageDto, String> colEquipmentCodeList;
    private Consumer<WindowObject> closeViewHandler;
    private WindowObject windowObject;
    private Stage stage;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(EquipmentPackageController.class.getResource("/com.vupt.SHM.views/EquipmentPackage.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.load();
            view.setId(AppConstants.MANAGE_EQUIPMENT_PACKAGE);
            EquipmentPackageController equipmentRequestController = loader.getController();
            equipmentRequestController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle(AppConstants.MANAGE_EQUIPMENT_PACKAGE);
            return equipmentRequestController.windowObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void init(Stage primaryStage, Consumer<WindowObject> closeViewHandler, Parent view) {
        this.stage = primaryStage;
        this.closeViewHandler = closeViewHandler;
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY), () -> search());
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_ANY), () -> closeView());
        this.windowObject = new WindowObject(view, this);
    }

    @Override
    @FXML
    public void initialize() {
        initTableView();
        tbEquipmentPackage.getItems().addAll(equipmentPackageService.findAllDto());
    }

    @Override
    public void initTableView() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colDetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        this.colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPrice.setCellFactory(column -> {
            return new TableCell<EquipmentPackageDto, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item == -1 ? "" : FormatUtils.formatVNDCurrency(item)); // Hiển thị tên sản phẩm
                    }
                }
            };
        });
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDate.setCellFactory(column -> {
            return new TableCell<EquipmentPackageDto, Date>() {
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
        this.colEquipmentCodeList.setCellValueFactory(new PropertyValueFactory<>("equipmentCodeList"));
    }

    @Override
    @FXML
    public void createEntityView() {
        EquipmentPackageSaving.loadView(this::save);
    }

    @Override
    @FXML
    public void updateEntityView() {
        EquipmentPackageDto equipmentPackageDto = tbEquipmentPackage.getSelectionModel().getSelectedItem();
        if (equipmentPackageDto == null) return;
        EquipmentPackageSaving.loadView(equipmentPackageDto, this::save);
    }

    @Override
    @FXML
    public void deleteEntityView() {
        EquipmentPackageDto equipmentPackageDto = this.tbEquipmentPackage.getSelectionModel().getSelectedItem();
        if (equipmentPackageDto == null) return;

        Optional<ButtonType> result = CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING)
                .setHeaderText(null)
                .setContentText(DisplayTextUtils.getWarningDeleteMessage(AppConstants.MENU_EQUIPMENT_PACKAGE, String.valueOf(equipmentPackageDto.getId())))
                .setYesNoButtonTypes()
                .build().showAndWait();

        if ((result.get()).getButtonData() == ButtonBar.ButtonData.YES) {
            try {
                this.equipmentPackageService.delete(equipmentPackageDto.getId());
                reload();
                CustomNotification.createNotification("Trạng thái", "Xóa thành công", this.tbEquipmentPackage).showInformation();
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
    public void save(EquipmentPackageDto equipmentPackageDto) {
        this.equipmentPackageService.save(equipmentPackageDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbEquipmentPackage).showInformation();
        reload();
    }

    @Override
    public void reload() {
        reload(equipmentPackageService.findAllDto());
    }

    private void reload(List<EquipmentPackageDto> equipmentPackageDtoList) {
        this.tbEquipmentPackage.getItems().clear();
        this.tbEquipmentPackage.getItems().addAll(equipmentPackageDtoList);
    }

    @Override
    @FXML
    public void refresh() {
      clear();
      reload();
    }

    @Override
    @FXML
    public void clear() {

    }

    @Override
    @FXML
    public void search() {

    }

    @Override
    @FXML
    public void closeView() {
        this.closeViewHandler.accept(windowObject);
    }
}
