package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.DepartmentSwitchReason;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.DepartmentSwitchReportDetailDto;
import com.vupt.SHM.dto.DepartmentSwitchReportExDto;
import com.vupt.SHM.dto.DepartmentSwitchReportFileDto;
import com.vupt.SHM.services.DepartmentSwitchReportFileService;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.utils.FileUtils;
import com.vupt.SHM.views.component.DepartmentSwitchReasonListCell;

import java.io.IOException;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DepartmentSwitchReportViewing {
    @Autowired
    DepartmentSwitchReportFileService departmentSwitchReportFileService;
    @FXML
    ComboBox<DepartmentDto> cbDepartmentFrom;
    @FXML
    ComboBox<DepartmentDto> cbDepartmentTo;
    @FXML
    DatePicker dpSwitchDate;
    @FXML
    ComboBox<DepartmentSwitchReason> cbSwitchReason;
    @FXML
    TextField tfMessage;
    @FXML
    TextField tfBEN_GIAO_A;
    @FXML
    TextField tfDIA_CHI_A;
    @FXML
    TextField tfDAI_DIEN_A;
    @FXML
    TextField tfCHUC_DANH_A;
    @FXML
    TextField tfBEN_NHAN_B;
    @FXML
    TextField tfDIA_CHI_B;

    @FXML
    TextField tfDAI_DIEN_B;
    @FXML
    TextField tfCHUC_DANH_B;
    @FXML
    TextField tfNOI_DIEN_RA;
    @FXML
    TableView<DepartmentSwitchReportDetailDto> tbReportDetail;
    @FXML
    TableColumn<DepartmentSwitchReportDetailDto, Integer> colSTT;
    @FXML
    TableColumn<DepartmentSwitchReportDetailDto, String> colEquipmentName;
    @FXML
    TableColumn<DepartmentSwitchReportDetailDto, String> colEquipmentCode;
    @FXML
    TableColumn<DepartmentSwitchReportDetailDto, String> colType;
    @FXML
    TableColumn<DepartmentSwitchReportDetailDto, Integer> colCount;
    @FXML
    TableColumn<DepartmentSwitchReportDetailDto, String> colEquipmentStatus;
    @FXML
    TableColumn<DepartmentSwitchReportDetailDto, String> colNote;
    @FXML
    HBox hBoxFileList;
    private DepartmentSwitchReportExDto departmentSwitchReportExDto;

    public static void loadView(DepartmentSwitchReportExDto departmentSwitchReportExDto) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader(DepartmentSwitchReportViewing.class.getResource("/com.vupt.SHM.views/popup/DepartmentSwitchReportView.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));
            stage.setTitle("[View]Chuyển đổi bộ phận thiết bị ID = " + departmentSwitchReportExDto.getId());

            com.vupt.SHM.views.popup.DepartmentSwitchReportViewing departmentSwitchReportViewing = loader.<com.vupt.SHM.views.popup.DepartmentSwitchReportViewing>getController();
            departmentSwitchReportViewing.init(departmentSwitchReportExDto);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
   public void initialize() {
        this.cbSwitchReason.setItems(FXCollections.observableArrayList(DepartmentSwitchReason.values()));
        this.cbSwitchReason.setCellFactory(param -> new DepartmentSwitchReasonListCell());
        this.cbSwitchReason.setButtonCell((ListCell<DepartmentSwitchReason>) new DepartmentSwitchReasonListCell());
        initTableView();
    }


    private void init(DepartmentSwitchReportExDto departmentSwitchReportExDto) {
        this.departmentSwitchReportExDto = departmentSwitchReportExDto;
        setValueToFrom();
    }

    private void initTableView() {
        this.colSTT.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(Integer.valueOf(this.tbReportDetail.getItems().indexOf(cellData.getValue()) + 1)));
        this.colEquipmentName.setCellValueFactory(cellData -> new SimpleStringProperty(((DepartmentSwitchReportDetailDto) cellData.getValue()).getEquipmentDto().getName()));
        this.colEquipmentCode.setCellValueFactory(cellData -> new SimpleStringProperty(((DepartmentSwitchReportDetailDto) cellData.getValue()).getEquipmentDto().getCode()));
        this.colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.colEquipmentStatus.setCellValueFactory(new PropertyValueFactory<>("equipmentStatus"));
        this.colNote.setCellValueFactory(new PropertyValueFactory<>("note"));
    }


    private void setValueToFrom() {
        this.cbDepartmentFrom.setValue(this.departmentSwitchReportExDto.getDepartmentFrom());
        this.cbDepartmentTo.setValue(this.departmentSwitchReportExDto.getDepartmentTo());
        this.dpSwitchDate.setValue(DateTimeUtils.convertToLocalDateViaSqlDate(this.departmentSwitchReportExDto.getSwitchDate()));
        this.cbSwitchReason.setValue(this.departmentSwitchReportExDto.getDepartmentSwitchReason());
        this.tfMessage.setText(this.departmentSwitchReportExDto.getMessage());
        this.tfBEN_GIAO_A.setText(this.departmentSwitchReportExDto.getBEN_A());
        this.tfDIA_CHI_A.setText(this.departmentSwitchReportExDto.getDIA_CHI_A());
        this.tfDAI_DIEN_A.setText(this.departmentSwitchReportExDto.getDAI_DIEN_A());
        this.tfCHUC_DANH_A.setText(this.departmentSwitchReportExDto.getCHUC_DANH_A());
        this.tfBEN_NHAN_B.setText(this.departmentSwitchReportExDto.getBEN_B());
        this.tfDIA_CHI_B.setText(this.departmentSwitchReportExDto.getDIA_CHI_B());
        this.tfDAI_DIEN_B.setText(this.departmentSwitchReportExDto.getDAI_DIEN_B());
        this.tfCHUC_DANH_B.setText(this.departmentSwitchReportExDto.getCHUC_DANH_B());
        this.tfNOI_DIEN_RA.setText(this.departmentSwitchReportExDto.getNOI_DIEN_RA());
        this.tbReportDetail.setItems(FXCollections.observableList(this.departmentSwitchReportExDto.getDepartmentSwitchReportDetailList()));
        this.departmentSwitchReportExDto.getDepartmentSwitchReportFileList().stream().forEach(departmentSwitchReportFileDto -> {
            Button btnFile = new Button(departmentSwitchReportFileDto.getFileName());
            btnFile.setOnAction(event->{
                try {
                    String localPath = String.format("%s/%s", FileUtils.getTempFolderPath(), departmentSwitchReportFileDto.getFileName());
                    departmentSwitchReportFileService.downloadFTPFile(localPath, departmentSwitchReportFileDto);
                    FileUtils.openFile(localPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.hBoxFileList.getChildren().add(btnFile);
        });
    }

    @FXML
    public void close() {
        this.tbReportDetail.getScene().getWindow().hide();
    }
}