package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.CategoryDto;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.services.CategoryService;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EquipmentService;
import com.vupt.SHM.views.BaseController;
import com.vupt.SHM.views.EmployeeController;
import com.vupt.SHM.views.IWindowController;
import com.vupt.SHM.views.component.WindowObject;

import java.util.function.Consumer;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StatisticController implements BaseController {
    @Autowired
    DepartmentService departmentService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    EquipmentService equipmentService;
    @FXML
    private ComboBox<DepartmentDto> cbDepartment;
    @FXML
    private BarChart<String, Number> barChart;
    private Consumer<WindowObject> closeViewHandler;
    private Parent view;
    private Stage stage;

    public static WindowObject loadView(Stage primaryStage, Consumer<WindowObject> closeViewHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(EmployeeController.class.getResource("/com.vupt.SHM.views/Statistic.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            Parent view = loader.<Parent>load();
            view.setId("Thống kê thiết bị");
            com.vupt.SHM.views.StatisticController statisticController = loader.<com.vupt.SHM.views.StatisticController>getController();
            statisticController.init(primaryStage, closeViewHandler, view);
            primaryStage.setTitle("Thống kê thiết bị");
            return new WindowObject(view, (IWindowController) statisticController);
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
        initChart();
        this.cbDepartment.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
    }


    public void initTableView() {
    }


    public void createEntityView() {
    }


    public void updateEntityView() {
    }


    public void deleteEntityView() {
    }


    public void save(Object o) {
    }


    @FXML
    public void search() {
        DepartmentDto departmentDto = this.cbDepartment.getValue();
        if (departmentDto != null) {
            filterChart(departmentDto);
        } else {
            initChart();
        }

    }


    public void reload() {
    }


    @FXML
    public void clear() {
        this.cbDepartment.setValue(null);
    }

    @FXML
    public void refresh() {
        initChart();
        clear();
    }

    private void initChart() {
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Thống kê thiết bị tổng hợp");
        this.categoryService.findAllDto().stream().forEach(categoryDto -> series1.getData().add(new XYChart.Data<>(categoryDto.getName(), Long.valueOf(this.equipmentService.count(categoryDto.getId())))));


        this.barChart.getData().clear();
        this.barChart.getData().add(series1);
        for (XYChart.Data<String, Number> data : series1.getData()) {
            Tooltip tooltip = new Tooltip(data.getYValue().toString());
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    public void filterChart(DepartmentDto departmentDto) {
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Thống kê thiết bị tổng hợp");
        this.categoryService.findAllDto().stream().forEach(categoryDto -> series1.getData().add(new XYChart.Data<>(categoryDto.getName(), Long.valueOf(this.equipmentService.count(categoryDto.getId(), departmentDto.getId())))));


        this.barChart.getData().clear();
        this.barChart.getData().add(series1);
        for (XYChart.Data<String, Number> data : series1.getData()) {
            Tooltip tooltip = new Tooltip(data.getYValue().toString());
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    @FXML
    public void closeView() {
        this.closeViewHandler.accept(new WindowObject(this.view, (IWindowController) this));
    }
}
