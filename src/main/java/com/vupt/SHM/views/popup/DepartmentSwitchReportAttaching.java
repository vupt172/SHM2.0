package com.vupt.SHM.views.popup;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.dto.DepartmentSwitchReportAttachingDto;
import com.vupt.SHM.dto.DepartmentSwitchReportFileDto;
import com.vupt.SHM.services.DepartmentSwitchReportFileService;
import com.vupt.SHM.utils.FileUtils;
import com.vupt.SHM.views.component.DepartmentSwitchReportAttachBox;

import java.io.File;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class DepartmentSwitchReportAttaching {
    @Autowired
    DepartmentSwitchReportFileService departmentSwitchReportFileService;
    @FXML
    ListView<DepartmentSwitchReportAttachBox> listBox;
    private DepartmentSwitchReportAttachingDto departmentSwitchReportAttachingDto;
    private BiConsumer<Long, List<DepartmentSwitchReportFileDto>> saveAttachFilesHandler;
    private Stage stage;

    public static void loadView(DepartmentSwitchReportAttachingDto departmentSwitchReportAttachingDto, BiConsumer<Long, List<DepartmentSwitchReportFileDto>> saveAttachFilesHandler) {
        try {
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader(DepartmentSwitchReportAttaching.class.getResource("/com.vupt.SHM.views/popup/DepartmentSwitchReportAttaching.fxml"));
            loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
            stage.setScene(new Scene(loader.<Parent>load()));

            DepartmentSwitchReportAttaching departmentSwitchReportAttaching = loader.getController();
            departmentSwitchReportAttaching.init(stage, departmentSwitchReportAttachingDto, saveAttachFilesHandler);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void initialize() {
    }

    private void init(Stage stage, DepartmentSwitchReportAttachingDto departmentSwitchReportAttachingDto, BiConsumer<Long, List<DepartmentSwitchReportFileDto>> saveAttachFilesHandler) {
        this.stage = stage;
        this.departmentSwitchReportAttachingDto = departmentSwitchReportAttachingDto;
        loadFiles();
        this.saveAttachFilesHandler = saveAttachFilesHandler;
    }

    private void loadFiles() {
        this.departmentSwitchReportAttachingDto.getDepartmentSwitchReportFileList().stream()
                .forEach(departmentSwitchReportFileDto -> {
                    DepartmentSwitchReportAttachBox departmentSwitchReportAttachBox = new DepartmentSwitchReportAttachBox(departmentSwitchReportFileDto, this::download, this::remove);
                    this.listBox.getItems().add(departmentSwitchReportAttachBox);
                });
    }

    @FXML
    public void uploadFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Chọn biên bản chuyển đổi");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Document File", new String[]{"*.pdf", "*.docx"}));
        File file = fc.showOpenDialog(this.stage);
        if (file != null) {
            DepartmentSwitchReportFileDto departmentSwitchReportFileDto = new DepartmentSwitchReportFileDto();
            departmentSwitchReportFileDto.setFileName(file.getName());
            departmentSwitchReportFileDto.setLocalPath(file.getPath());
            DepartmentSwitchReportAttachBox departmentSwitchReportAttachBox = new DepartmentSwitchReportAttachBox(departmentSwitchReportFileDto, this::remove);
            this.listBox.getItems().add(departmentSwitchReportAttachBox);
        }
    }


    private void remove(DepartmentSwitchReportAttachBox departmentSwitchReportAttachBox) {
        this.listBox.getItems().remove(departmentSwitchReportAttachBox);
    }

    public void download(DepartmentSwitchReportAttachBox departmentSwitchReportAttachBox) {
        try {
            DepartmentSwitchReportFileDto departmentSwitchReportFileDto = departmentSwitchReportAttachBox.getDepartmentSwitchReportFileDto();
            String localPath = String.format("%s/%s", new Object[]{FileUtils.getTempFolderPath(), departmentSwitchReportFileDto.getFileName()});
            this.departmentSwitchReportFileService.downloadFTPFile(localPath, departmentSwitchReportFileDto);
            FileUtils.openFile(localPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void save() {
        List<DepartmentSwitchReportFileDto> departmentSwitchReportFileDtoList = (List<DepartmentSwitchReportFileDto>) this.listBox.getItems().stream().map(departmentSwitchReportAttachBox -> departmentSwitchReportAttachBox.getDepartmentSwitchReportFileDto()).collect(Collectors.toList());
        this.saveAttachFilesHandler.accept(Long.valueOf(this.departmentSwitchReportAttachingDto.getId()), departmentSwitchReportFileDtoList);
        close();
    }

    @FXML
    public void close() {
        this.stage.hide();
    }
}
