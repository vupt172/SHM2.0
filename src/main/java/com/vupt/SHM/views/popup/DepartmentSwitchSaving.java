package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.DepartmentSwitchReason;
import com.vupt.SHM.dto.*;
import com.vupt.SHM.entity.DepartmentSwitchReport;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EquipmentService;
import com.vupt.SHM.utils.DateTimeUtils;
import com.vupt.SHM.views.component.AutoCompleteBox;
import com.vupt.SHM.views.component.DepartmentConverter;
import com.vupt.SHM.views.component.DepartmentSwitchReasonListCell;

import java.sql.Date;
import java.time.LocalDate;
import java.util.function.Consumer;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class DepartmentSwitchSaving {
    @Autowired
    EquipmentService equipmentService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    DepartmentConverter departmentConverter;
    @FXML
    Label lbTitle;
    @FXML
    TabPane tabPane;
    @FXML
    Tab tabA;
    @FXML
    Tab tabB;
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

    public static void loadView(Consumer<DepartmentSwitchReportSavingDto> saveHandler) {
        /* 124 */
        loadView(null, saveHandler);
    }

    @FXML
    Button btnNext;
    @FXML
    Label lbMessage;
    @FXML
    Button btnConfirm;
    @FXML
    TextField tfId;
    @FXML
    ComboBox<EquipmentDto> cbEquipment;
    @FXML
    TextField tfDVT;
    @FXML
    Spinner<Integer> spSL;
    @FXML
    TextField tfTTTB;
    @FXML
    TextArea taNote;
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
    private SpinnerValueFactory<Integer> valueFactory;
    private ObservableList<DepartmentSwitchReportDetailDto> reportDetailDtoList;
    private Consumer<DepartmentSwitchReportSavingDto> saveHandler;
    private DepartmentSwitchReportSavingDto departmentSwitchReportSavingDto;

    public static void loadView(DepartmentSwitchReportSavingDto departmentSwitchReportSavingDto, Consumer<DepartmentSwitchReportSavingDto> saveHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(DepartmentSwitchSaving.class.getResource("/com.vupt.SHM.views/popup/DepartmentSwitchSave.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));
            DepartmentSwitchSaving departmentSwitchSaving = loader.getController();
            departmentSwitchSaving.init(departmentSwitchReportSavingDto, saveHandler);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
        this.tabB.setDisable(true);
        this.btnNext.setDisable(true);
        this.spSL.setValueFactory(this.valueFactory);
        this.cbDepartmentFrom.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        this.cbDepartmentTo.setItems(FXCollections.observableList(this.departmentService.findAllDtoIgnoreSuspended()));
        this.cbDepartmentFrom.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        this.cbDepartmentTo.setConverter((StringConverter<DepartmentDto>) this.departmentConverter);
        AutoCompleteBox.build(this.cbDepartmentFrom, this.cbDepartmentTo);
        AutoCompleteBox.build(this.cbDepartmentTo, this.dpSwitchDate);
        this.cbSwitchReason.getItems().addAll(DepartmentSwitchReason.values());
        this.cbSwitchReason.setCellFactory(param -> new DepartmentSwitchReasonListCell());
        this.cbSwitchReason.setButtonCell((ListCell<DepartmentSwitchReason>) new DepartmentSwitchReasonListCell());
        this.cbEquipment.setItems(FXCollections.observableList(this.equipmentService.findAll()));


        this.colSTT.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(Integer.valueOf(this.tbReportDetail.getItems().indexOf(cellData.getValue()) + 1)));
        this.colEquipmentName.setCellValueFactory(cellData -> new SimpleStringProperty(((DepartmentSwitchReportDetailDto) cellData.getValue()).getEquipmentDto().getName()));
        this.colEquipmentCode.setCellValueFactory(cellData -> new SimpleStringProperty(((DepartmentSwitchReportDetailDto) cellData.getValue()).getEquipmentDto().getCode()));
        this.colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.colCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.colEquipmentStatus.setCellValueFactory(new PropertyValueFactory<>("equipmentStatus"));
        this.colNote.setCellValueFactory(new PropertyValueFactory<>("note"));


        this.valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        this.valueFactory.setValue(Integer.valueOf(1));
        this.tbReportDetail.setOnMouseClicked(event -> {setReportDetailFromValue(tbReportDetail.getSelectionModel().getSelectedItem());});
    }


    private void init(DepartmentSwitchReportSavingDto departmentSwitchReportSavingDto, Consumer<DepartmentSwitchReportSavingDto> saveHandler) {
        this.saveHandler = saveHandler;
        this.tfId.setEditable(false);
        if (departmentSwitchReportSavingDto == null) {
            this.departmentSwitchReportSavingDto = new DepartmentSwitchReportSavingDto();
            this.lbTitle.setText("THÊM MỚI BIÊN BẢN CHUYỂN ĐỔI BỘ PHẬN THIẾT BỊ");
            this.dpSwitchDate.setValue(LocalDate.now());
            this.tfDIA_CHI_A.setText("Bệnh viện đa khoa khu vực Long Thành");
            this.tfDIA_CHI_B.setText("Bệnh viện đa khoa khu vực Long Thành");
            this.reportDetailDtoList = FXCollections.observableArrayList();
        } else {
            this.departmentSwitchReportSavingDto = departmentSwitchReportSavingDto;
            this.lbTitle.setText("CẬP NHẬT BIÊN BẢN CHUYỂN ĐỔI BỘ PHẬN THIẾT BỊ");
            setFormValue();
            this.reportDetailDtoList = FXCollections.observableList(this.departmentSwitchReportSavingDto.getDepartmentSwitchReportDetailList());
        }
        this.tbReportDetail.setItems(this.reportDetailDtoList);
    }


    private boolean checkValidValue() {
        if (this.cbDepartmentFrom.getValue() == null) {
            this.lbMessage.setText("Bên giao không được để trống");
            return false;
        }
        if (this.cbDepartmentTo.getValue() == null) {
            this.lbMessage.setText("Bên nhận không được để trống");
            return false;
        }
        if (this.cbSwitchReason.getValue() == null) {
            this.lbMessage.setText("Mục đích không được để trống");
            return false;
        }
        if (this.dpSwitchDate.getValue() == null) {
            this.lbMessage.setText("Ngày không được để trống");
            return false;
        }

        return true;
    }

    @FXML
    public void confirm() {
        if (!checkValidValue()) return;
        disableForm();
        this.btnConfirm.setDisable(true);
        this.btnNext.setDisable(false);
    }

    private void disableForm() {
        this.cbDepartmentFrom.setDisable(true);
        this.cbDepartmentTo.setDisable(true);
        this.cbSwitchReason.setDisable(true);
        this.dpSwitchDate.setDisable(true);
        this.tfMessage.setEditable(false);
        this.tfBEN_GIAO_A.setEditable(false);
        this.tfDIA_CHI_A.setEditable(false);
        this.tfDAI_DIEN_A.setEditable(false);
        this.tfCHUC_DANH_A.setEditable(false);
        this.tfBEN_NHAN_B.setEditable(false);
        this.tfCHUC_DANH_B.setEditable(false);
        this.tfDIA_CHI_B.setEditable(false);
        this.tfDAI_DIEN_B.setEditable(false);
    }

    @FXML
    public void nextTabB() {
        this.tabB.setDisable(false);
        this.tabPane.getSelectionModel().select(this.tabB);
        this.tabA.setDisable(true);
        this.cbEquipment.setItems(FXCollections.observableList(this.equipmentService.findByDepartmentId(((DepartmentDto) this.cbDepartmentFrom.getValue()).getId())));
    }

    @FXML
    public void backTabA() {
        this.tabA.setDisable(false);
        this.tabPane.getSelectionModel().select(this.tabA);
        this.tabB.setDisable(true);
    }


    public boolean contain(EquipmentDto equipmentDto) {
        return this.reportDetailDtoList.stream().anyMatch(reportDetailDto -> reportDetailDto.getEquipmentDto().equals(equipmentDto));
    }

    private DepartmentSwitchReportDetailDto getDtoValue() {
        DepartmentSwitchReportDetailDto reportDetailDto = new DepartmentSwitchReportDetailDto();
        reportDetailDto.setEquipmentDto(this.cbEquipment.getValue());
        reportDetailDto.setType(this.tfDVT.getText());
        reportDetailDto.setCount(Integer.valueOf(((Integer) this.spSL.getValue()).intValue()).intValue());
        reportDetailDto.setEquipmentStatus(this.tfTTTB.getText());
        reportDetailDto.setNote(this.taNote.getText());

        return reportDetailDto;
    }

    private void setReportDetailFromValue(DepartmentSwitchReportDetailDto reportDetailDto) {
        this.tfId.setText(String.valueOf(reportDetailDto.getId()));
        this.cbEquipment.setValue(reportDetailDto.getEquipmentDto());
        this.tfDVT.setText(reportDetailDto.getType());

        this.valueFactory.setValue(Integer.valueOf(reportDetailDto.getCount()));
        this.spSL.setValueFactory(this.valueFactory);
        this.tfTTTB.setText(reportDetailDto.getEquipmentStatus());
        this.taNote.setText(reportDetailDto.getNote());
    }

    private void setFormValue() {
        this.cbDepartmentFrom.setValue(this.departmentSwitchReportSavingDto.getDepartmentFrom());
        this.cbDepartmentTo.setValue(this.departmentSwitchReportSavingDto.getDepartmentTo());
        this.dpSwitchDate.setValue(DateTimeUtils.convertToLocalDateViaSqlDate(this.departmentSwitchReportSavingDto.getSwitchDate()));
        this.tfMessage.setText(this.departmentSwitchReportSavingDto.getMessage());
        this.cbSwitchReason.setValue(this.departmentSwitchReportSavingDto.getDepartmentSwitchReason());

        this.tfBEN_GIAO_A.setText(this.departmentSwitchReportSavingDto.getBEN_A());
        this.tfDAI_DIEN_A.setText(this.departmentSwitchReportSavingDto.getDAI_DIEN_A());
        this.tfDIA_CHI_A.setText(this.departmentSwitchReportSavingDto.getDIA_CHI_A());
        this.tfCHUC_DANH_A.setText(this.departmentSwitchReportSavingDto.getCHUC_DANH_A());

        this.tfBEN_NHAN_B.setText(this.departmentSwitchReportSavingDto.getBEN_B());
        this.tfDAI_DIEN_B.setText(this.departmentSwitchReportSavingDto.getDAI_DIEN_B());
        this.tfDIA_CHI_B.setText(this.departmentSwitchReportSavingDto.getDIA_CHI_B());
        this.tfCHUC_DANH_B.setText(this.departmentSwitchReportSavingDto.getCHUC_DANH_B());
        this.tfNOI_DIEN_RA.setText(this.departmentSwitchReportSavingDto.getNOI_DIEN_RA());
    }

    private DepartmentSwitchReportSavingDto setDtoValue() {
        DepartmentSwitchReportSavingDto departmentSwitchReportSavingDto=new DepartmentSwitchReportSavingDto();
        departmentSwitchReportSavingDto.setId(this.departmentSwitchReportSavingDto.getId());
        departmentSwitchReportSavingDto.setDepartmentFrom(this.cbDepartmentFrom.getValue());
        departmentSwitchReportSavingDto.setDepartmentTo(this.cbDepartmentTo.getValue());
        departmentSwitchReportSavingDto.setSwitchDate(Date.valueOf(this.dpSwitchDate.getValue()));
        departmentSwitchReportSavingDto.setDepartmentSwitchReason(this.cbSwitchReason.getValue());
        departmentSwitchReportSavingDto.setMessage(this.tfMessage.getText());

        departmentSwitchReportSavingDto.setBEN_A(this.tfBEN_GIAO_A.getText());
        departmentSwitchReportSavingDto.setDIA_CHI_A(this.tfDIA_CHI_A.getText());
        departmentSwitchReportSavingDto.setCHUC_DANH_A(this.tfCHUC_DANH_A.getText());
        departmentSwitchReportSavingDto.setDAI_DIEN_A(this.tfDAI_DIEN_A.getText());

        departmentSwitchReportSavingDto.setBEN_B(this.tfBEN_NHAN_B.getText());
        departmentSwitchReportSavingDto.setDIA_CHI_B(this.tfDIA_CHI_B.getText());
        departmentSwitchReportSavingDto.setCHUC_DANH_B(this.tfCHUC_DANH_B.getText());
        departmentSwitchReportSavingDto.setDAI_DIEN_B(this.tfDAI_DIEN_B.getText());
        departmentSwitchReportSavingDto.setNOI_DIEN_RA(this.tfNOI_DIEN_RA.getText());
        departmentSwitchReportSavingDto.setDepartmentSwitchReportDetailList(this.reportDetailDtoList);
        return departmentSwitchReportSavingDto;
    }

    @FXML
    public void add() {
        DepartmentSwitchReportDetailDto reportDetailDto = getDtoValue();
        if (contain(reportDetailDto.getEquipmentDto())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Thiết bị này đã được thêm vào", new ButtonType[]{ButtonType.OK});
            alert.setHeaderText(null);
            alert.show();
            return;
        }
        this.reportDetailDtoList.add(reportDetailDto);
    }

    @FXML
    public void clear() {
        this.tfId.setText("");
        this.cbEquipment.setValue((EquipmentDto) null);
        this.tfDVT.setText(null);
        this.valueFactory.setValue(Integer.valueOf(1));
        this.spSL.setValueFactory(this.valueFactory);
        this.tfTTTB.setText("");
        this.taNote.setText("");
    }

    @FXML
    public void remove() {
        DepartmentSwitchReportDetailDto reportDetailDto = this.tbReportDetail.getSelectionModel().getSelectedItem();
        if (reportDetailDto != null)
            this.reportDetailDtoList.remove(reportDetailDto);
    }

    @FXML
    public void save() {
        this.departmentSwitchReportSavingDto=setDtoValue();
        this.saveHandler.accept(this.departmentSwitchReportSavingDto);
        close();
    }

    @FXML
    public void close() {
        this.btnConfirm.getScene().getWindow().hide();
    }
}

