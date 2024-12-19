package com.vupt.SHM.views;

import com.vupt.SHM.entity.AppVersion;
import com.vupt.SHM.repositories.AppVersionRepository;
import com.vupt.SHM.views.common.CustomAlert;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class AppVersionController {
    @Value("{config.app.version}")
    String version;
    @Autowired
    AppVersionRepository appVersionRepository;

    public boolean checkVersionValidation() {
        AppVersion appVersion = this.appVersionRepository.findAppVersionWithMaxId();
        if (appVersion == null || !appVersion.getName().equals(this.version)) {
            CustomAlert.AlertBuilder.builder(Alert.AlertType.WARNING)
                    .setTitle("Thông báo hệ thống")
                    .setHeaderText(null)
                    .setContentText("Cần cập nhật phiên bản mới")
                    .build().show();
            return false;
        }
        return true;
    }
}

