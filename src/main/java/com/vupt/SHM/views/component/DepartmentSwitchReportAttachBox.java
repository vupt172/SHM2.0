package com.vupt.SHM.views.component;

import com.vupt.SHM.dto.DepartmentSwitchReportFileDto;

import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Data;

@Data
public class DepartmentSwitchReportAttachBox extends HBox {
    private DepartmentSwitchReportFileDto departmentSwitchReportFileDto;
    private Consumer<DepartmentSwitchReportAttachBox> removeFileHandler;
    private Label lbFileName;
    private Button btnDelete;
    private Button btnDownload;

    public DepartmentSwitchReportAttachBox(DepartmentSwitchReportFileDto departmentSwitchReportFileDto, Consumer<com.vupt.SHM.views.component.DepartmentSwitchReportAttachBox> removeFileHandler) {
        this(departmentSwitchReportFileDto, null, removeFileHandler);
    }


    public DepartmentSwitchReportAttachBox(DepartmentSwitchReportFileDto departmentSwitchReportFileDto, Consumer<DepartmentSwitchReportAttachBox> downloadHandler, Consumer<DepartmentSwitchReportAttachBox> removeFileHandler) {
        this.departmentSwitchReportFileDto = departmentSwitchReportFileDto;
        this.removeFileHandler = removeFileHandler;
        setSpacing(5.0D);
        this.lbFileName = new Label(departmentSwitchReportFileDto.getFileName());
        this.lbFileName.setMaxWidth(Double.MAX_VALUE);
        this.btnDelete = new Button("x");

        this.btnDownload = new Button("Download");
        this.btnDownload.setVisible(false);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(new Node[]{this.lbFileName, this.btnDownload, this.btnDelete});
        setHgrow(this.lbFileName, Priority.ALWAYS);

        if (downloadHandler != null) {
            this.btnDownload.setVisible(true);
            this.btnDownload.setOnAction(event -> downloadHandler.accept(this));
        }

        this.btnDelete.setOnAction(event -> removeFileHandler.accept(this));
    }
}
