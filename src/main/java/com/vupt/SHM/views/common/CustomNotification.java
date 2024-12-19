package com.vupt.SHM.views.common;

import javafx.geometry.Pos;
/*    */ import javafx.util.Duration;
/*    */ import org.controlsfx.control.Notifications;

public class CustomNotification {
    public static Notifications createNotification(String title, String message, Object owner) {
        return Notifications.create()
                .title(title)
                .text(message)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(2.0D))
                .owner(owner);
    }
}
