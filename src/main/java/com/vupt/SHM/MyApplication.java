package com.vupt.SHM;

import com.vupt.SHM.constant.AppConfig;
import com.vupt.SHM.views.LoginController;
import com.vupt.SHM.views.exception.ErrorDialog;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class MyApplication
        extends Application {
    private static ConfigurableApplicationContext applicationContext;

    public void init() throws Exception {
        this.applicationContext = SpringApplication.run(MyApplication.class);
        System.setProperty("java.awt.headless", "false");
    }


    public void stop() throws Exception {
        applicationContext.close();
    }


    public void start(Stage primaryStage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(ErrorDialog::showError);
        if ((AppConfig.getInstance()).isValidVersion) {
            LoginController.loadView();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }
}

