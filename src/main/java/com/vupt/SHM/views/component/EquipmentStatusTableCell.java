package com.vupt.SHM.views.component;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.constant.EquipmentStatus;
import com.vupt.SHM.dto.EquipmentDto;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class EquipmentStatusTableCell extends TableCell<EquipmentDto, EquipmentStatus> {
    private final ImageView imageView = new ImageView();

    @Override
    protected void updateItem(EquipmentStatus item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setText(null);
        } else {
            Image imageNew = new Image(MyApplication.class.getResourceAsStream("/images/EquipmentStatusNew.png"),15,15,true,true);
            Image imageUsed = new Image(MyApplication.class.getResourceAsStream("/images/EquipmentStatusUsed.png"),15,15,true,true);
            Image imageStorage = new Image(MyApplication.class.getResourceAsStream("/images/EquipmentStatusStorage.png"),15,15,true,true);
            Image imageDestroy = new Image(MyApplication.class.getResourceAsStream("/images/EquipmentStatusDestroy.png"),15,15,true,true);

            switch(item){
                case NEW:
                    imageView.setImage(imageNew);
                    break;
                case USED:
                    imageView.setImage(imageUsed);
                    break;
                case STORAGE:
                    imageView.setImage(imageStorage);
                    break;
                case DESTROYED:
                    imageView.setImage(imageDestroy);
                    break;
            }
            setText(item.getTitle());
            setGraphic(imageView);
        }
    }

    public static Callback<TableColumn<EquipmentDto, EquipmentStatus>, TableCell<EquipmentDto, EquipmentStatus>> forTableColumn() {
        return param -> new EquipmentStatusTableCell();
    }
}
