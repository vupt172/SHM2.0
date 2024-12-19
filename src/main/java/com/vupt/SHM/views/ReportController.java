package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.views.EquipmentReportDetailController;
import com.vupt.SHM.views.EquipmentReportGeneralController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.component.WindowObject;

import java.io.IOException;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.springframework.stereotype.Controller;

@Controller
public class ReportController
        implements IWindowController {
    @FXML
    TreeView<String> treeView;
    @FXML
    StackPane stackPane;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.ReportController.class.getResource("/com.vupt.SHM.views/Report.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Hệ thống báo cáo");

            com.vupt.SHM.views.ReportController reportController = loader.<com.vupt.SHM.views.ReportController>getController();
            reportController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Hệ thống báo cáo");
            return new WindowObject(view, reportController);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public void init(Stage primaryStage, Consumer<WindowObject> closeViewHandler, Parent view) {
        this.stage = primaryStage;
        this.closeViewHandler = closeViewHandler;
        this.view = view;

        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_ANY), () -> search());
        this.stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_ANY), () -> closeView());    }


    @FXML
    public void initialize() {
        TreeItem<String> rootItem = new TreeItem<>("Hệ Thống Báo Cáo");
        TreeItem<String> equipmentReportItem = new TreeItem<>("Báo Cáo Thiết Bị");

        equipmentReportItem.getChildren().add(new TreeItem<>("Báo Cáo Thiết Bị Tổng Hợp"));
        equipmentReportItem.getChildren().add(new TreeItem<>("Báo Cáo Thiết Bị Theo Bộ Phận"));
        equipmentReportItem.setExpanded(true);

        TreeItem<String> item2 = new TreeItem<>("Báo Cáo Sửa Chữa");

        rootItem.getChildren().addAll(new TreeItem[]{equipmentReportItem, item2});
        rootItem.setExpanded(true);
        this.treeView.setRoot(rootItem);
        this.treeView.setShowRoot(true);

        this.treeView.setOnMouseClicked(event -> {
            TreeItem<String> selectedItem = this.treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                switch ((String) selectedItem.getValue()) {
                    case "Báo Cáo Thiết Bị Theo Bộ Phận":
                        try {
                            Parent view = EquipmentReportDetailController.loadView((Stage) this.stackPane.getScene().getWindow());
                            this.stackPane.getChildren().add(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "Báo Cáo Thiết Bị Tổng Hợp":
                        try {
                            Parent view = EquipmentReportGeneralController.loadView((Stage) this.stackPane.getScene().getWindow());
                            this.stackPane.getChildren().add(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }


    @FXML
    public void search() {
    }


    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, this));
    }
}
