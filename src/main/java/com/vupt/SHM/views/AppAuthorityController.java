package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.entity.AppAuthority;
import com.vupt.SHM.entity.AppFunction;
import com.vupt.SHM.services.AppAuthorityService;
import com.vupt.SHM.services.AppFunctionService;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CellValueFactoryCreator;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.popup.AppAuthoritySaving;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AppAuthorityController
        implements BaseController<AppAuthority> {
    @Autowired
    AppAuthorityService appAuthorityService;
    @Autowired
    AppFunctionService appFunctionService;
    @FXML
    TableView<AppAuthority> tbAuthority;
    @FXML
    TableColumn<AppAuthority, Long> colId;
    @FXML
    TableColumn<AppAuthority, String> colName;
    @FXML
    TableColumn<AppAuthority, String> colCode;
    @FXML
    TableColumn<AppAuthority, AppFunction> colAppFunction;
    @FXML
    ComboBox<AppFunction> cbAppFunction;
    @FXML
    Button btnSearch;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.AppAuthorityController.class.getResource("/com.vupt.SHM.views/AppAuthority.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Danh sách quyền");

            AppAuthorityController appAuthorityController = loader.<com.vupt.SHM.views.AppAuthorityController>getController();
            appAuthorityController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Danh sách quyền");
            return new WindowObject(view, (IWindowController) appAuthorityController);
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
        initTableView();
        this.tbAuthority.setItems(FXCollections.observableList(this.appAuthorityService.findAll()));
        this.cbAppFunction.setItems(FXCollections.observableList(this.appFunctionService.findAll()));
    }


    public void initTableView() {
        CellValueFactoryCreator.setAppAuthorityTableViewCellFactory(this.colId, this.colName, this.colCode, this.colAppFunction);
    }


    @FXML
    public void createEntityView() {
        AppAuthoritySaving.loadView(this::save);
    }


    @FXML
    public void updateEntityView() {
    }


    @FXML
    public void deleteEntityView() {
    }


    public void save(AppAuthority appAuthority) {
        this.appAuthorityService.save(appAuthority);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbAuthority).showInformation();
        if (this.cbAppFunction.getValue() == null) {
            reload();
        } else {
            reload(this.cbAppFunction.getValue());
        }
    }

    public void reload() {
        reload(this.appAuthorityService.findAll());
    }


    @FXML
    public void refresh() {
        reload();
        clear();
        this.cbAppFunction.setItems(FXCollections.observableList(this.appFunctionService.findAll()));
    }


    @FXML
    public void clear() {
        this.cbAppFunction.setValue(null);
    }


    @FXML
    public void search() {
        AppFunction appFunction = this.cbAppFunction.getValue();
        long appFunctionId = (appFunction != null) ? appFunction.getId() : 0L;

        List<AppAuthority> appAuthorities = this.appAuthorityService.search(appFunctionId);
        reload(appAuthorities);
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, (IWindowController) this));
    }


    private void reload(List<AppAuthority> appAuthorities) {
        this.tbAuthority.getItems().clear();
        this.tbAuthority.setItems(FXCollections.observableList(appAuthorities));
    }

    private void reload(AppFunction appFunction) {
        List<AppAuthority> appAuthorities = this.appAuthorityService.findByAppFunction(appFunction.getId());
        reload(appAuthorities);
    }
}
