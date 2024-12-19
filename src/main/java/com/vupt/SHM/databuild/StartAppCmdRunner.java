package com.vupt.SHM.databuild;

import com.vupt.SHM.constant.AppConfig;
import com.vupt.SHM.entity.AppVersion;
import com.vupt.SHM.repositories.AppVersionRepository;
import com.vupt.SHM.views.common.CustomAlert;

import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StartAppCmdRunner {
    @Value("${config.app.version}")
    String version;
    @Autowired
    AppVersionRepository appVersionRepository;

    @Bean
    public CommandLineRunner getCommandLineRunner() {
        return args -> {
            AppVersion appVersion = this.appVersionRepository.findAppVersionWithMaxId();
            if (appVersion == null || !appVersion.getName().equals(this.version)) {
                Platform.runLater(() -> {
                    CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING)
                            .setTitle("Thông báo hệ thống")
                            .setHeaderText(null)
                            .setContentText("Cần cập nhật phiên bản mới nhất")
                            .build().show();
                });
            } else {
                AppConfig.getInstance().isValidVersion = true;
            }
        };
    }
}

