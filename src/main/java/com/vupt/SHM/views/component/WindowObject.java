package com.vupt.SHM.views.component;


import com.vupt.SHM.views.IWindowController;
import javafx.scene.Parent;
import lombok.Data;

@Data
public class WindowObject {
    private IWindowController controller;
    private Parent view;


    public WindowObject(Parent view, IWindowController controller) {
        this.view = view;
        this.controller = controller;
    }
}

