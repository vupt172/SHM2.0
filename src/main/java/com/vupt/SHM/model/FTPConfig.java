package com.vupt.SHM.model;

import lombok.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class FTPConfig {
    @Value("${config.ftp.host}")
    private String host;
    @Value("${config.ftp.port}")
    private int port;
    @Value("${config.ftp.username}")
    private String username;
    @Value("${config.ftp.password}")
    private String password;
    @Value("${config.ftp.DepartmentSwitchReportsFolder}")
    private String departmentSwitchReportsFolder;


    public void showInfo() {
        System.out.println("FTP Config Info: ");
        System.out.println("Host: " + this.host);
        System.out.println("Post: " + this.port);
        System.out.println("Username: " + this.username);
        System.out.println("Password: " + this.password);
    }
}

