package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.AuthorityCode;
import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.services.CategoryService;
import com.vupt.SHM.utils.AuthenticationUtils;
import com.vupt.SHM.utils.DisplayTextUtils;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CustomAlert;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.exception.ErrorDialog;
import com.vupt.SHM.views.popup.CategoryEditing;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
public class CategoryController implements BaseController<CategoryDto> {
    @Autowired
    private CategoryService categoryService;
    @FXML
    private Button btnCreate;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<CategoryDto> tbCategory;
    @FXML
    private TableColumn<CategoryDto, Long> colId;
    @FXML
    private TableColumn<CategoryDto, String> colName;
    @FXML
    private TableColumn<CategoryDto, String> colCode;
    @FXML
    private TableColumn<CategoryDto, Boolean> colIsSuspended;
    @FXML
    private TextField tfName;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;
    private WindowObject windowObject;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(CategoryController.class.getResource("/com.vupt.SHM.views/Category.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Quản lý danh mục");
            CategoryController categoryController = loader.getController();
            categoryController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Quản lý danh mục");
            WindowObject windowObject = new WindowObject(view,categoryController);
            return windowObject;
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
        this.tbCategory.setItems(FXCollections.observableList(this.categoryService.findAllDto()));
    }

    public void initAppFunctionByRoleAndAuthorities() {
        AuthenticationUtils authenticationUtils = AuthenticationUtils.getCurrentAuthenticationInfo();
        if (!authenticationUtils.hasAuthority(AuthorityCode.CREATE_CATEGORY.getAuthority()))
            this.btnCreate.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.UPDATE_CATEGORY.getAuthority()))
            this.btnUpdate.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.DELETE_CATEGORY.getAuthority()))
            this.btnDelete.setDisable(true);
        if (!authenticationUtils.hasAuthority(AuthorityCode.FIND_CATEGORY.getAuthority()))
            this.btnSearch.setDisable(true);

    }


    public void initTableView() {
        this.colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colCode.setCellValueFactory(new PropertyValueFactory<>("code"));

        this.colIsSuspended.setCellValueFactory(param -> new SimpleBooleanProperty(((CategoryDto) param.getValue()).isSuspended()));
        this.colIsSuspended.setCellFactory(CheckBoxTableCell.forTableColumn(this.colIsSuspended));
    }


    @FXML
    public void createEntityView() {
        CategoryEditing.loadView(this::save);
    }


    @FXML
    public void updateEntityView() {
        CategoryDto selectedCategoryDTO = this.tbCategory.getSelectionModel().getSelectedItem();
        if (selectedCategoryDTO == null)
            return;
        CategoryEditing.loadView(selectedCategoryDTO, this::save);
    }


    @FXML
    public void deleteEntityView() {
        CategoryDto selectedCategoryDTO = this.tbCategory.getSelectionModel().getSelectedItem();

        if (selectedCategoryDTO == null) {
            return;
        }


        Optional<ButtonType> result = CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING).setHeaderText(null).setContentText(DisplayTextUtils.getWarningDeleteMessage("Danh mục", selectedCategoryDTO.getName())).setYesNoButtonTypes().build().showAndWait();
        if (((ButtonType) result.get()).getButtonData() == ButtonBar.ButtonData.YES) {
            try {
                this.categoryService.delete(selectedCategoryDTO.getId());
                reload();
                CustomNotification.createNotification("Trạng thái", "Xóa thành công", this.tbCategory).showInformation();
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


    public void save(CategoryDto categoryDto) {
        this.categoryService.save(categoryDto);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbCategory).showInformation();
        reload();
    }


    @FXML
    public void reload() {
        reload(this.categoryService.findAllDto());
    }


    @FXML
    public void refresh() {
        reload();
        clear();
    }


    @FXML
    public void clear() {
        this.tfName.setText("");
    }


    @FXML
    public void search() {
        String categoryName = this.tfName.getText();
        List<CategoryDto> categoryDtoList = this.categoryService.search(categoryName);
        reload(categoryDtoList);
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, this));
    }

    private void reload(List<CategoryDto> categoryDtoList) {
        this.tbCategory.getItems().clear();
        this.tbCategory.setItems(FXCollections.observableList(categoryDtoList));
    }
}
