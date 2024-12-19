package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.TreeNodeType;
import com.vupt.SHM.dto.AccountExDto;
import com.vupt.SHM.entity.AppAuthority;
import com.vupt.SHM.entity.AppFunction;
import com.vupt.SHM.entity.Role;
import com.vupt.SHM.model.TreeNode;
import com.vupt.SHM.services.AppAuthorityService;
import com.vupt.SHM.services.AppFunctionService;
import com.vupt.SHM.services.RoleService;
import com.vupt.SHM.views.component.AppCheckBoxTreeCell;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AccountRolesAdjusting {
    @Autowired
    RoleService roleService;
    @Autowired
    AppFunctionService appFunctionService;
    @Autowired
    AppAuthorityService appAuthorityService;
    @FXML
    Label lbAccountInfo;
    @FXML
    TabPane tabPane;
    @FXML
    TreeView<TreeNode> tvAccountRoles;
    @FXML
    TreeView<TreeNode> tvAccountAuthorities;
    private AccountExDto accountExDto;
    private Consumer<AccountExDto> saveHandler;

    public static void loadView(AccountExDto accountExDto, Consumer<AccountExDto> saveHandler) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setMaximized(true);
            FXMLLoader loader = new FXMLLoader(AccountRolesAdjusting.class.getResource("/com.vupt.SHM.views/popup/AccountRolesAdjust.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));

            AccountRolesAdjusting accountAuthorityController = loader.getController();
            accountAuthorityController.init(accountExDto, saveHandler);

            stage.show();
        } catch (Exception e) {
            /*  65 */
            e.printStackTrace();
        }
    }

    private void init(AccountExDto accountExDTO, Consumer<AccountExDto> saveHandler) {
        this.accountExDto = accountExDTO;
        this.saveHandler = saveHandler;
        this.lbAccountInfo.setText(String.format("[%s]%s", accountExDTO.getUsername(), "Granted Roles"));
        this.tvAccountAuthorities.setEditable(false);
        initAccountRoleTreeView();
        initAccountAuthorityTreeView();

        if (accountExDTO.getRoles().size() > 0) {
            initRoles();
            initAuthorities();
        }
    }


    private void initRoles() {
        List<Role> grantedRoles = this.accountExDto.getRoles();
        this.tvAccountRoles.getRoot().getChildren().stream()
                .forEach(roleItem -> {
                    Role role = (Role) (roleItem.getValue().getContent());
                    if (grantedRoles.stream().anyMatch(grantedRole -> grantedRole.getCode().equals(role.getCode()))) {
                        ((CheckBoxTreeItem) roleItem).setSelected(true);
                    }
                });
    }

    private void initRoleAuthorities(Role role) {
        role = this.roleService.findById(role.getId());
        List<AppAuthority> grantedAuthorities = role.getAuthorities();
        this.tvAccountAuthorities.getRoot().getChildren().stream()
                .forEach(appFunctionItem ->
                {
                    appFunctionItem.getChildren().forEach(
                            authorityItem -> {
                                System.out.println(authorityItem.getValue().getContent());
                                AppAuthority appAuthority = (AppAuthority) (authorityItem.getValue().getContent());
                                if (grantedAuthorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getCode().equals(appAuthority.getCode())))
                                    ((CheckBoxTreeItem) authorityItem).setSelected(true);
                            }
                    );
                });
    }


    private void initAuthorities() {
        this.accountExDto.getRoles().forEach(role -> initRoleAuthorities(role));
    }

    private void clearAuthorities() {
        this.tvAccountAuthorities.getRoot().getChildren().forEach(
                appFunctionItem ->
                        appFunctionItem.getChildren()
                                .forEach(
                                        authorityItem -> ((CheckBoxTreeItem) authorityItem).setSelected(false)
                                ));
    }


    private void initAccountRoleTreeView() {
        CheckBoxTreeItem<TreeNode> rootItem = new CheckBoxTreeItem<>(new TreeNode("Granted Roles", TreeNodeType.COMMON));
        List<CheckBoxTreeItem<TreeNode>> roleItems = (List<CheckBoxTreeItem<TreeNode>>) this.roleService.findAll().stream().map(role -> new CheckBoxTreeItem<>(new TreeNode(role, TreeNodeType.ROLE))).collect(Collectors.toList());
        rootItem.getChildren().addAll(roleItems);
        this.tvAccountRoles.setRoot(rootItem);
        this.tvAccountRoles.setShowRoot(false);
        this.tvAccountRoles.setCellFactory(param -> new AppCheckBoxTreeCell());
    }

    private void initAccountAuthorityTreeView() {
        CheckBoxTreeItem<TreeNode> rootItem = new CheckBoxTreeItem<>(new TreeNode("Granted Authorities", TreeNodeType.COMMON));

        List<CheckBoxTreeItem<TreeNode>> appFunctionItems = this.appFunctionService.findAll().stream()
                .map(appFunction -> {
                    CheckBoxTreeItem<TreeNode> appFunctionItem = new CheckBoxTreeItem<>(new TreeNode(appFunction, TreeNodeType.APP_FUNCTION));
                    List<AppAuthority> appAuthorityList = this.appAuthorityService.findByAppFunction(appFunction.getId());
                    List<CheckBoxTreeItem<TreeNode>> appAuthorityItems = appAuthorityList.stream().map(appAuthority -> new CheckBoxTreeItem<>(new TreeNode(appAuthority, TreeNodeType.APP_AUTHORITY))).collect(Collectors.toList());
                    appFunctionItem.getChildren().addAll(appAuthorityItems);
                    appFunctionItem.setExpanded(true);
                    return appFunctionItem;
                }).collect(Collectors.toList());

        rootItem.getChildren().addAll(appFunctionItems);
        this.tvAccountAuthorities.setRoot(rootItem);
        this.tvAccountAuthorities.setShowRoot(false);
        this.tvAccountAuthorities.setCellFactory(param -> new AppCheckBoxTreeCell());
    }


    @FXML
    public void viewAuthorities() {
        clearAuthorities();
        setAccountExDto();
        initAuthorities();
        this.tabPane.getSelectionModel().select(1);
    }

    public void setAccountExDto() {
        List<Role> selectedRoles = new ArrayList<>();
        this.tvAccountRoles.getRoot().getChildren().stream()
                .forEach(roleItem -> {
                    boolean isSelectedRole = ((CheckBoxTreeItem) roleItem).isSelected();
                    if (isSelectedRole) {
                        selectedRoles.add((Role) ((TreeNode) roleItem.getValue()).getContent());
                    }
                });

        this.accountExDto.setRoles(selectedRoles);
    }

    @FXML
    public void save() {
        setAccountExDto();
        this.saveHandler.accept(this.accountExDto);
        close();
    }

    @FXML
    public void close() {
        this.tvAccountRoles.getScene().getWindow().hide();
    }
}
