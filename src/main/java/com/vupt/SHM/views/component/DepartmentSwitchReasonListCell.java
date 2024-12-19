package com.vupt.SHM.views.component;

import com.vupt.SHM.constant.DepartmentSwitchReason;
import javafx.scene.control.ListCell;


public class DepartmentSwitchReasonListCell extends ListCell<DepartmentSwitchReason> {
    protected void updateItem(DepartmentSwitchReason item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {

            setText(item.getReason());
        }
    }
}

