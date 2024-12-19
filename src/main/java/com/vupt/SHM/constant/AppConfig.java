package com.vupt.SHM.constant;

public class AppConfig {
    private static com.vupt.SHM.constant.AppConfig instance;

    public static synchronized com.vupt.SHM.constant.AppConfig getInstance() {
        if (instance == null) {
            instance = new com.vupt.SHM.constant.AppConfig();
        }
        return instance;
    }

    public boolean isValidVersion = false;
}

