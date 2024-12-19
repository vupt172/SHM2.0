package com.vupt.SHM.views.component;

import com.vupt.SHM.constant.DepartmentType;
import javafx.scene.control.ListCell;

public class DepartmentTypeListCell
        extends ListCell<DepartmentType> {
    protected void updateItem(DepartmentType item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setText(null);
        } else {

            setText(item.getTitle());
        }
    }
}