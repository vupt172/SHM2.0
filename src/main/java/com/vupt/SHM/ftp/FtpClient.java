package com.vupt.SHM.ftp;

import com.vupt.SHM.exceptions.FTPUploadFileException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import lombok.Data;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

@Data
public class FtpClient {
    private String server;
    private int port;
    private String user;
    private String password;
    private FTPClient ftp;


    public FtpClient(String server, int port, String user, String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void open() throws IOException {
        this.ftp = new FTPClient();

        this.ftp.addProtocolCommandListener((ProtocolCommandListener) new PrintCommandListener(new PrintWriter(System.out)));

        this.ftp.connect(this.server, this.port);
        int reply = this.ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            this.ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }


        boolean login = this.ftp.login(this.user, this.password);
        if (login) {
            this.ftp.enterLocalPassiveMode();
            this.ftp.setFileType(2);
            this.ftp.setFileTransferMode(2);

            System.out.println("Logged in successfully.");
        } else {

            System.out.println("Login failed.");
            close();
        }
    }

    public Collection<String> listFiles(String path) throws IOException {
        FTPFile[] files = this.ftp.listFiles(path);
        return (Collection<String>) Arrays.<FTPFile>stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }

    public void uploadFile(String localPath, String remotePath) throws IOException, FTPUploadFileException {
        System.out.println(localPath);
        FileInputStream fis = new FileInputStream(localPath);
        boolean done = this.ftp.storeFile(remotePath, fis);
        if (done) {
            System.out.println("The file is uploaded successfully.");
        } else {
            throw new FTPUploadFileException("Could not upload the file.");
        }
        fis.close();
    }

    public void downloadFile(String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        boolean success = this.ftp.retrieveFile(source, out);
        if (success) {
            System.out.println("File downloaded successfully.");
        } else {
            System.out.println("Failed to download file.");
        }
        out.close();
    }

    public void deleteFile(String remotePath) throws IOException {
        boolean deleted = this.ftp.deleteFile(remotePath);
        if (deleted) {
            System.out.println("The file was deleted successfully.");
        } else {
            throw new FTPUploadFileException("Could not delete the file. It may not exist or you may not have permission.");
        }
    }


    public void createDirectory(String newDir) throws IOException {
        boolean created = this.ftp.makeDirectory(newDir);
        if (created) {
            System.out.println("Directory created successfully!");
        } else {
            System.out.println("Failed to create directory.");
        }
    }

    public void selectDirectory(String directory) throws IOException {
        boolean changed = this.ftp.changeWorkingDirectory(directory);
        if (changed) {
            System.out.println("Changed to directory: " + directory);
        } else {
            System.out.println("Could not change to directory: " + directory);
        }
    }

    public void close() throws IOException {
        this.ftp.disconnect();
    }
}


