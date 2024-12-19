package com.vupt.SHM.services;

import com.vupt.SHM.dto.DepartmentSwitchReportAttachingDto;
import com.vupt.SHM.dto.DepartmentSwitchReportFileDto;
import com.vupt.SHM.entity.DepartmentSwitchReport;
import com.vupt.SHM.entity.DepartmentSwitchReportFile;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.FTPUploadFileException;
import com.vupt.SHM.ftp.FtpClient;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.model.FTPConfig;
import com.vupt.SHM.repositories.DepartmentSwitchReportFileRepository;
import com.vupt.SHM.services.DepartmentSwitchReportService;

import java.io.IOException;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DepartmentSwitchReportFileService {
    @Autowired
    DepartmentSwitchReportService departmentSwitchReportService;
    @Autowired
    DepartmentSwitchReportFileRepository departmentSwitchReportFileRepository;
    @Autowired
    MapstructMapper mapstructMapper;
    @Autowired
    private FTPConfig ftpConfig;

    public void saveAll(long departmentSwitchReportId, List<DepartmentSwitchReportFileDto> departmentSwitchReportFileDtoList) {
        DepartmentSwitchReport departmentSwitchReport = this.departmentSwitchReportService.findDepartmentSwitchReportWithFiles(departmentSwitchReportId);
        DepartmentSwitchReportAttachingDto departmentSwitchReportAttachingDto = this.mapstructMapper.departmentSwitchReportToDepartmentSwitchReportAttachingDto(departmentSwitchReport);
        departmentSwitchReportAttachingDto.getDepartmentSwitchReportFileList().stream()
                .filter(departmentSwitchReportFileDto -> (departmentSwitchReportFileDto.getId() > 0L))
                .filter(departmentSwitchReportFileDto -> {
                    boolean isExistFile = departmentSwitchReportFileDtoList.contains(departmentSwitchReportFileDto);

                    return !isExistFile;
                }).forEach(departmentSwitchReportFileDto -> {
            try {
                delete(departmentSwitchReportFileDto);
            } catch (IOException e) {
                throw new AppException("Đã có lỗi xảy ra khi delete file FTP server");
            }
        });
        departmentSwitchReportFileDtoList.stream().forEach(departmentSwitchReportFileDto -> {
            try {
                if (departmentSwitchReportFileDto.getId() == 0L)
                    save(departmentSwitchReportId, departmentSwitchReportFileDto);
            } catch (Exception e) {
                throw new AppException("Đã có lỗi xảy ra khi upload file FTP server");
            }
        });
    }

    @Transactional
    public void save(long departmentSwitchReportId, DepartmentSwitchReportFileDto departmentSwitchReportFileDto) throws IOException {
        DepartmentSwitchReport departmentSwitchReport = this.departmentSwitchReportService.findDepartmentSwitchReportWithFiles(departmentSwitchReportId);
        DepartmentSwitchReportFile departmentSwitchReportFile = this.mapstructMapper.departmentSwitchReportFileDtoToDepartmentSwitchReportFile(departmentSwitchReportFileDto);
        departmentSwitchReportFile.setDepartmentSwitchReport(departmentSwitchReport);
        String remotePath = uploadFTPFile(departmentSwitchReportId, departmentSwitchReportFileDto);
        departmentSwitchReportFile.setRemotePath(remotePath);
        this.departmentSwitchReportFileRepository.save(departmentSwitchReportFile);
    }

    @Transactional
    public void delete(DepartmentSwitchReportFileDto departmentSwitchReportFileDto) throws IOException {
        this.departmentSwitchReportFileRepository.deleteById(Long.valueOf(departmentSwitchReportFileDto.getId()));
        FtpClient ftpClient = new FtpClient(this.ftpConfig.getHost(), this.ftpConfig.getPort(), this.ftpConfig.getUsername(), this.ftpConfig.getPassword());
        ftpClient.open();
        ftpClient.deleteFile(departmentSwitchReportFileDto.getRemotePath());
        ftpClient.close();
    }

    public DepartmentSwitchReportFile findById(long id) {
        return (DepartmentSwitchReportFile) this.departmentSwitchReportFileRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new AppException("Không tìm thấy file với id = " + id));
    }

    public static String getRemotePath(String folder, Long departmentSwitchReportId, String fileName) {
        return String.format("/%s/%d/%s", new Object[]{folder, departmentSwitchReportId, fileName});
    }


    public String uploadFTPFile(long departmentSwitchReportId, DepartmentSwitchReportFileDto departmentSwitchReportFileDto) throws IOException, FTPUploadFileException {
        System.out.println("UploadFTPFile");
        this.ftpConfig.showInfo();
        FtpClient ftpClient = new FtpClient(this.ftpConfig.getHost(), this.ftpConfig.getPort(), this.ftpConfig.getUsername(), this.ftpConfig.getPassword());
        ftpClient.open();
        ftpClient.selectDirectory(this.ftpConfig.getDepartmentSwitchReportsFolder());
        ftpClient.createDirectory(String.valueOf(departmentSwitchReportId));
        String remotePath = getRemotePath(this.ftpConfig.getDepartmentSwitchReportsFolder(), Long.valueOf(departmentSwitchReportId), departmentSwitchReportFileDto.getFileName());
        ftpClient.uploadFile(departmentSwitchReportFileDto.getLocalPath(), remotePath);
        ftpClient.close();
        return remotePath;
    }

    public void downloadFTPFile(String localPath, DepartmentSwitchReportFileDto departmentSwitchReportFileDto) throws IOException {
        System.out.println("Download FTP File");
        FtpClient ftpClient = new FtpClient(this.ftpConfig.getHost(), this.ftpConfig.getPort(), this.ftpConfig.getUsername(), this.ftpConfig.getPassword());
        ftpClient.open();
        String remotePath = departmentSwitchReportFileDto.getRemotePath();

        ftpClient.downloadFile(remotePath, localPath);
        ftpClient.close();
    }
}
