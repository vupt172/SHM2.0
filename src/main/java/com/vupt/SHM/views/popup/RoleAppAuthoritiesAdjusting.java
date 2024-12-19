package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.TreeNodeType;
import com.vupt.SHM.entity.AppAuthority;
import com.vupt.SHM.entity.AppFunction;
import com.vupt.SHM.entity.Role;
import com.vupt.SHM.model.TreeNode;
import com.vupt.SHM.services.AppAuthorityService;
import com.vupt.SHM.services.AppFunctionService;
import com.vupt.SHM.services.RoleService;
import com.vupt.SHM.views.component.AppCheckBoxTreeCell;
import com.vupt.SHM.views.exception.ErrorDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class RoleAppAuthoritiesAdjusting {
    @Autowired
    AppAuthorityService appAuthorityService;
    @Autowired
    AppFunctionService appFunctionService;
    @Autowired
    RoleService roleService;
    @FXML
    private Label lbTitle;
    @FXML
    private TreeView<TreeNode> tvAuthorities;
    private Role role;
    private Consumer<Role> saveHandler;

    public static void loadView(Role role, Consumer<Role> saveHandler) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.popup.RoleAppAuthoritiesAdjusting.class.getResource("/com.vupt.SHM.views/popup/RoleAppAuthoritiesAdjust.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));
            stage.setMaximized(true);


            com.vupt.SHM.views.popup.RoleAppAuthoritiesAdjusting roleAppAuthoritiesAdjusting = loader.<com.vupt.SHM.views.popup.RoleAppAuthoritiesAdjusting>getController();
            roleAppAuthoritiesAdjusting.init(role, saveHandler);

            stage.show();
        } catch (Exception e) {
            ErrorDialog.showError(null, e);
        }
    }

    private void init(Role role, Consumer<Role> saveHandler) {
        this.role = this.roleService.findById(role.getId());
        this.saveHandler = saveHandler;
        initCheckBoxTreeView();
        if (this.role.getAuthorities().size() > 0) {
            initAuthorities();
        }
        this.lbTitle.setText(String.format("[%s] Granted Authorities", new Object[]{role.getCode()}));
    }


    private void initAuthorities() {
        List<AppAuthority> appAuthorities = this.role.getAuthorities();
        this.tvAuthorities.getRoot().getChildren().stream()
                .forEach(appFunctionNode -> appFunctionNode.getChildren().forEach(
                        authorityItem -> {
                            if (appAuthorities.stream().anyMatch(appAuthority -> appAuthority.getCode().equals(((AppAuthority)authorityItem.getValue().getContent()).getCode())))
                                ((CheckBoxTreeItem) authorityItem).setSelected(true);
                        }));
    }


    private void initCheckBoxTreeView() {
        CheckBoxTreeItem<TreeNode> rootItem = new CheckBoxTreeItem<>(new TreeNode("Granted Authorities", TreeNodeType.COMMON));

        List<CheckBoxTreeItem<TreeNode>> itemLevel1List = this.appFunctionService.findAll().stream().map(appFunction -> {
            CheckBoxTreeItem<TreeNode> itemLevel1 = new CheckBoxTreeItem<>(new TreeNode(appFunction, TreeNodeType.APP_FUNCTION));
            List<CheckBoxTreeItem<TreeNode>> itemLevel2List = this.appAuthorityService.findByAppFunction(appFunction.getId()).stream()
                    .map(appAuthority -> new CheckBoxTreeItem<>(new TreeNode(appAuthority, TreeNodeType.APP_AUTHORITY)))
                    .collect(Collectors.toList());
            itemLevel1.getChildren().addAll(itemLevel2List);
            itemLevel1.setExpanded(true);
            return itemLevel1;
        }).collect(Collectors.toList());

        rootItem.getChildren().addAll(itemLevel1List);
        this.tvAuthorities.setRoot(rootItem);
        this.tvAuthorities.setShowRoot(false);
        this.tvAuthorities.setCellFactory(param -> new AppCheckBoxTreeCell());
    }


    @FXML
    public void save() {
        List<AppAuthority> appAuthorities = new ArrayList<>();
        this.tvAuthorities.getRoot().getChildren().stream()
                .forEach(functionItem ->
                        functionItem.getChildren().stream().forEach(authorityItem -> {
                            if (((CheckBoxTreeItem) authorityItem).isSelected())
                                appAuthorities.add((AppAuthority) authorityItem.getValue().getContent());
                        }));


        this.role.setAuthorities(appAuthorities);
        this.saveHandler.accept(this.role);
        close();
    }

    @FXML
    public void close() {
        this.tvAuthorities.getScene().getWindow().hide();
    }
}


