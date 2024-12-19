package com.vupt.SHM.views.component;

import com.vupt.SHM.constant.EquipmentStatus;
import javafx.scene.control.ListCell;

public class EquipmentStatusListCell
        extends ListCell<EquipmentStatus> {
    protected void updateItem(EquipmentStatus item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {

            setText(item.getTitle());
        }
    }
}


