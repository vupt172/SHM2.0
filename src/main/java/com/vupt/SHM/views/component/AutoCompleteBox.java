package com.vupt.SHM.views.component;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class AutoCompleteBox<T> implements EventHandler {
    private ComboBox<T> comboBox;
    private Node nextNode;

    public static <T> com.vupt.SHM.views.component.AutoCompleteBox build(ComboBox<T> comboBox, Node nextNode) {
        return new com.vupt.SHM.views.component.AutoCompleteBox<>(comboBox, nextNode);
    }

    private final ObservableList<T> data;
    private Integer sid;

    public static <T> com.vupt.SHM.views.component.AutoCompleteBox build(ComboBox<T> comboBox) {
        return new com.vupt.SHM.views.component.AutoCompleteBox<>(comboBox);
    }
    public static <T> com.vupt.SHM.views.component.AutoCompleteBox build(ComboBox<T> comboBox,StringConverter<T> converter) {
        return new com.vupt.SHM.views.component.AutoCompleteBox<>(comboBox,converter);
    }

    public AutoCompleteBox(ComboBox<T> comboBox) {
        this.comboBox = comboBox;
        this.data = comboBox.getItems();
        doAutoCompleteBox();
    }
    public AutoCompleteBox(ComboBox<T> comboBox,StringConverter<T> converter) {
        this.comboBox = comboBox;
        this.comboBox.setConverter(converter);
        this.data = comboBox.getItems();
        doAutoCompleteBox();
    }


    public AutoCompleteBox(ComboBox<T> comboBox, Node nextNode) {
        this.comboBox = comboBox;
        this.nextNode = nextNode;
        this.data = comboBox.getItems();
        doAutoCompleteBox();
    }

    public AutoCompleteBox(ComboBox<T> comboBox, Integer sid) {
        this.comboBox = comboBox;
        this.data = comboBox.getItems();
        this.sid = sid;
      //  this.comboBox.setConverter((StringConverter<T>) new Object(this));
        doAutoCompleteBox();
    }


    private void doAutoCompleteBox() {
        this.comboBox.setEditable(true);
        this.comboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.booleanValue()) {
                this.comboBox.show();
            }
        });

        this.comboBox.getEditor().setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    return;
                }
            }

            this.comboBox.show();
        });
        this.comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> moveCaret(this.comboBox.getEditor().getText().length()));
        this.comboBox.setOnKeyPressed(t -> this.comboBox.hide());
        this.comboBox.setOnKeyReleased(this);
        if (this.sid != null) {
            this.comboBox.getSelectionModel().select(this.sid.intValue());
        }
    }

    public void handle(Event event) {
        KeyCode keyCode = ((KeyEvent) event).getCode();
        if (keyCode == KeyCode.UP || keyCode == KeyCode.DOWN || keyCode == KeyCode.RIGHT || keyCode == KeyCode.LEFT || keyCode == KeyCode.HOME || keyCode == KeyCode.END || keyCode == KeyCode.TAB) {
            return;
        }
        if (keyCode == KeyCode.BACK_SPACE) {
            String str = this.comboBox.getEditor().getText();
            if (str != null && str.length() > 0) {
                str = str.substring(0, str.length());
            }
            if (str != null) {
                this.comboBox.getEditor().setText(str);
                moveCaret(str.length());
            }
            this.comboBox.getSelectionModel().clearSelection();
        }

        if (keyCode == KeyCode.ENTER && this.comboBox.getSelectionModel().getSelectedIndex() > -1) {
            if (this.nextNode != null) this.nextNode.requestFocus();
            return;
        }
        setItems();
    }


    private void setItems() {
        ObservableList<T> list = FXCollections.observableArrayList();
        for (T datum : this.data) {
            String s = this.comboBox.getEditor().getText().toLowerCase();
            if (datum.toString().toLowerCase().contains(s.toLowerCase())) {
                list.add(datum);
            }
        }

        if (list.isEmpty()) this.comboBox.hide();
        this.comboBox.setItems(list);
        this.comboBox.show();
    }

    private void moveCaret(int textLength) {
        this.comboBox.getEditor().positionCaret(textLength);
    }
}

