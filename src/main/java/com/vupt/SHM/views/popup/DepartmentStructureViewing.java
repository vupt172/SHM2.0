package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.TreeNodeType;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.model.TreeNode;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.views.component.AppCheckBoxTreeCell;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class DepartmentStructureViewing {
    @Autowired
    DepartmentService departmentService;

    public static void loadView() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(DepartmentStructureViewing.class.getResource("/com.vupt.SHM.views/popup/DepartmentStructure.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));
            stage.setTitle("Sơ đồ cấu trúc bộ phận");

            DepartmentStructureViewing departmentStructureViewing = loader.getController();
            departmentStructureViewing.init();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    TreeView<TreeNode> tvDepartmentStructure;
    @FXML
    Button btnClose;

    void init() {
        initTreeView();
    }

    private void initTreeView() {
        CheckBoxTreeItem<TreeNode> rootItem = new CheckBoxTreeItem<>(new TreeNode("Department Structure", TreeNodeType.COMMON));

        List<DepartmentDto> rootDepartmentList = this.departmentService.findAllDtoIgnoreSuspended();
        List<CheckBoxTreeItem<TreeNode>> rootDepartmentTreeItemList = new ArrayList<>();
        rootDepartmentList.stream().forEach(rootDepartmentDto -> {
            CheckBoxTreeItem<TreeNode> rootDepartmentItem = new CheckBoxTreeItem<>(new TreeNode(rootDepartmentDto, TreeNodeType.COMMON));
            List<CheckBoxTreeItem<TreeNode>> childDepartmentList = getChildDepartmentTreeItems(rootDepartmentDto);
            rootDepartmentItem.getChildren().addAll(childDepartmentList);
            rootDepartmentItem.setExpanded(true);
            rootDepartmentTreeItemList.add(rootDepartmentItem);
        });
        rootItem.getChildren().addAll(rootDepartmentTreeItemList);
        rootItem.setExpanded(true);
        rootItem.setSelected(true);
        this.tvDepartmentStructure.setRoot(rootItem);
        this.tvDepartmentStructure.setShowRoot(true);
        this.tvDepartmentStructure.setEditable(false);
        this.tvDepartmentStructure.setCellFactory(param -> new AppCheckBoxTreeCell());
    }

    private List<CheckBoxTreeItem<TreeNode>> getChildDepartmentTreeItems(DepartmentDto departmentDto) {
        List<DepartmentDto> childDepartmentDtoList = this.departmentService.getChildDepartmentsByIdIgnoreSuspended(departmentDto.getId());
        List<CheckBoxTreeItem<TreeNode>> treeItemList = new ArrayList<>();
        childDepartmentDtoList.stream().forEach(d -> {
            CheckBoxTreeItem<TreeNode> departmentTreeItem = new CheckBoxTreeItem<>(new TreeNode(d, TreeNodeType.COMMON));

            treeItemList.add(departmentTreeItem);
        });
        return treeItemList;
    }

    @FXML
    public void close() {
        this.btnClose.getScene().getWindow().hide();
    }
}

