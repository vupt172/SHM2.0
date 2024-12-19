package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.entity.Role;
import com.vupt.SHM.services.RoleService;
import com.vupt.SHM.views.AppAuthorityController;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.common.CellValueFactoryCreator;
import com.vupt.SHM.views.common.CustomNotification;
import com.vupt.SHM.views.component.WindowObject;
import com.vupt.SHM.views.popup.RoleAppAuthoritiesAdjusting;
import com.vupt.SHM.views.popup.RoleSaving;

import java.util.List;
import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
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
public class RoleController
        implements BaseController<Role> {
    @Autowired
    RoleService roleService;
    @FXML
    TextField tfCode;
    @FXML
    TableView<Role> tbRole;
    @FXML
    TableColumn<Role, Long> colId;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(AppAuthorityController.class.getResource("/com.vupt.SHM.views/Role.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Danh sách vai trò");

            RoleController roleController = loader.getController();
            roleController.init(primaryStage, closeViewHandler, view);

            primaryStage.setTitle("Danh sách vai trò");
            return new WindowObject(view, (IWindowController) roleController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    TableColumn<Role, String> colName;
    @FXML
    TableColumn<Role, String> colCode;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public void init(Stage primaryStage, Consumer<WindowObject> closeViewHandler, Parent view) {
        this.stage = primaryStage;
        this.closeViewHandler = closeViewHandler;
        this.view = view;
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY), () -> search());
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_ANY), () -> closeView());
    }


    @FXML
    public void initialize() {
        initTableView();
        this.tbRole.setContextMenu(initContextMenu());
        reload();
    }


    public void initTableView() {
        CellValueFactoryCreator.setRoleTableViewCellFactory(this.colId, this.colName, this.colCode);
    }


    @FXML
    public void createEntityView() {
        RoleSaving.loadView(this::save);
    }


    @FXML
    public void updateEntityView() {
    }


    @FXML
    public void deleteEntityView() {
    }


    public void save(Role role) {
        this.roleService.save(role);
        CustomNotification.createNotification("Trạng thái", "Lưu thành công", this.tbRole).showInformation();
        reload();
    }


    public void reload() {
        reload(this.roleService.findAll());
    }


    @FXML
    public void refresh() {
        reload();
        clear();
    }


    @FXML
    public void clear() {
        this.tfCode.setText("");
    }


    @FXML
    public void search() {
        String code = this.tfCode.getText();
        List<Role> roleList = this.roleService.search(code);
        reload(roleList);
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, (IWindowController) this));
    }


    public ContextMenu initContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem itemAuthorities = new MenuItem("Adjust granted authoritites");

        itemAuthorities.setOnAction(e -> {
            Role selectedRole = this.tbRole.getSelectionModel().getSelectedItem();
            if (selectedRole != null) {
                RoleAppAuthoritiesAdjusting.loadView(selectedRole, this::save);
            }
        });
        contextMenu.getItems().add(itemAuthorities);
        return contextMenu;
    }

    private void reload(List<Role> roleList) {
        this.tbRole.getItems().clear();
        this.tbRole.setItems(FXCollections.observableList(roleList));
    }
}
