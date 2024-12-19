package com.vupt.SHM.views;

import com.vupt.SHM.MyApplication;
import com.vupt.SHM.security.UserDetailsServiceImpl;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class LoginController {
    private Stage primaryStage;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;

    public static void loadView() throws IOException {
        FXMLLoader loader = new FXMLLoader(com.vupt.SHM.views.LoginController.class.getResource("/com.vupt.SHM.views/Login.fxml"));
        loader.setControllerFactory(MyApplication.getApplicationContext()::getBean);
        Parent view = loader.<Parent>load();
        Stage stage = new Stage(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(LoginController.class.getResourceAsStream("/images/app_icon.png")));
        stage.setScene(new Scene(view));
        com.vupt.SHM.views.LoginController controller = loader.<com.vupt.SHM.views.LoginController>getController();

        controller.primaryStage = stage;
        stage.show();
    }

    @FXML
    Button loginBtn;
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Label message;

    @FXML
    public void initialize() {
        this.password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login();
            }
        });
    }


    @FXML
    public void login() {
        try {
            if (this.username.getText().trim().equals("")) {
                this.message.setText("Tài khoản không được để trống");
            } else if (this.password.getText().trim().equals("")) {
                this.message.setText("Mật khẩu không được để trống");
            } else {
                Authentication authentication = this.authenticationManager.authenticate((Authentication) new UsernamePasswordAuthenticationToken(this.username
                        .getText(), this.password.getText()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                SHMController.loadView();
                close();
            }

        } catch (BadCredentialsException | org.springframework.security.authentication.InternalAuthenticationServiceException e) {
            this.message.setText("Tài khoản hoặc mật khẩu không chính xác!");
            e.printStackTrace();
        } catch (LockedException e) {
            this.message.setText("Tài khoản bị khóa");
        } catch (Exception e) {
            System.out.print("LoginController.class :");
            e.printStackTrace();
        }
    }


    @FXML
    private void close() {
        this.loginBtn.getScene().getWindow().hide();
    }
}

