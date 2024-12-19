package com.vupt.SHM.utils;

import com.vupt.SHM.dto.EquipmentDto;
import com.vupt.SHM.views.popup.EquipmentDetail;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import lombok.Data;

@Data
public class EquipmentDetailMouseClickHandler implements EventHandler<MouseEvent> {
    private TableView<EquipmentDto> tbEquipment;

    public void setTbEquipment(TableView<EquipmentDto> tbEquipment) {
        this.tbEquipment = tbEquipment;
    }

    public EquipmentDetailMouseClickHandler(TableView<EquipmentDto> tbView) {
        this.tbEquipment = tbView;
    }

    public void handle(MouseEvent event) {
        if (event.getClickCount() == 2) {
            EquipmentDto equipmentDTO = this.tbEquipment.getSelectionModel().getSelectedItem();
            if (equipmentDTO != null)
                EquipmentDetail.loadView(equipmentDTO);
        }
    }
}
