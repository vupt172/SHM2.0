package com.vupt.SHM.views.exception;

import com.vupt.SHM.views.common.CustomAlert;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class ErrorDialog {
    public static void showError(Thread t, Throwable e) {
        System.err.println("***Default exception handler***");
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        } else {
            System.err.println("An unexpected error occurred in " + t);
        }
        e.printStackTrace();
    }

    private static String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String s = sw.toString();
        return s;
    }

    public static void showErrorDialog(Throwable e) {
        VBox dialogPaneContent = new VBox();
        Label label = new Label("Stack Trace");
        String stackTrace = getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);

        dialogPaneContent.getChildren().addAll(new Node[]{label, textArea});
        CustomAlert.AlertBuilder.builder(Alert.AlertType.ERROR)
                .setTitle("Thông báo hệ thống")
                .setHeaderText(e.getMessage())
                .setDialogPaneContent(dialogPaneContent)
                .build().show();
    }
}

