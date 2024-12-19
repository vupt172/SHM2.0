package com.vupt.SHM.services;

import com.vupt.SHM.dto.DepartmentSwitchReportDetailDto;
import com.vupt.SHM.dto.DepartmentSwitchReportDto;
import com.vupt.SHM.dto.DepartmentSwitchReportExDto;
import com.vupt.SHM.dto.DepartmentSwitchReportSavingDto;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.DepartmentSwitchReport;
import com.vupt.SHM.entity.DepartmentSwitchReportDetail;
import com.vupt.SHM.entity.Equipment;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.DepartmentSwitchReportDetailRepository;
import com.vupt.SHM.repositories.DepartmentSwitchReportRepository;
import com.vupt.SHM.repositories.EquipmentRepository;
import com.vupt.SHM.services.EquipmentHistoryService;
import com.vupt.SHM.specifications.DepartmentSwitchReportSpecification;
import com.vupt.SHM.utils.DisplayTextUtils;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class DepartmentSwitchReportService {
    @Autowired
    DepartmentSwitchReportRepository departmentSwitchReportRepository;
    @Autowired
    DepartmentSwitchReportDetailRepository departmentSwitchReportDetailRepository;

    @Transactional
    public void save(DepartmentSwitchReportSavingDto departmentSwitchReportExDto) {
        DepartmentSwitchReport departmentSwitchReport;
        if (departmentSwitchReportExDto.getId() == 0L) {
            departmentSwitchReport = this.mapstructMapper.departmentSwitchReportExDtoToDepartmentSwitchReport(departmentSwitchReportExDto);

            departmentSwitchReport.getDepartmentSwitchReportDetailList().stream().forEach(departmentSwitchReportDetail -> departmentSwitchReportDetail.setDepartmentSwitchReport(departmentSwitchReport));
        } else {
            departmentSwitchReport = findById(departmentSwitchReportExDto.getId());
            if (departmentSwitchReport.isFlush()) {
                throw new AppException("Biên bản này đã duyệt không thể cập nhật!");
            }
            departmentSwitchReport.getDepartmentSwitchReportDetailList().stream()
                    .filter(departmentSwitchReportDetail -> (departmentSwitchReportDetail.getId() > 0L))
                    .filter(departmentSwitchReportDetail -> {
                        DepartmentSwitchReportDetailDto departmentSwitchReportDetailDto = this.mapstructMapper.departmentSwitchReportDetailToDepartmentSwitchReportDetailDto(departmentSwitchReportDetail);

                        boolean isExist = departmentSwitchReportExDto.getDepartmentSwitchReportDetailList().contains(departmentSwitchReportDetailDto);
                        return !isExist;
                    }).forEach(departmentSwitchReportDetail -> this.departmentSwitchReportDetailRepository.delete(departmentSwitchReportDetail));

            this.mapstructMapper.departmentSwitchReportExDtoToSelectedDepartmentSwitchReport(departmentSwitchReportExDto, departmentSwitchReport);
            departmentSwitchReport.getDepartmentSwitchReportDetailList().stream().forEach(departmentSwitchReportDetail -> departmentSwitchReportDetail.setDepartmentSwitchReport(departmentSwitchReport));
        }
        this.departmentSwitchReportRepository.save(departmentSwitchReport);
    }

    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    MapstructMapper mapstructMapper;
    @Autowired
    EquipmentHistoryService equipmentHistoryService;

    public List<DepartmentSwitchReportDto> findAll() {
        return this.mapstructMapper.departmentSwitchReportsToDepartmentSwitchReportDtos(this.departmentSwitchReportRepository.findAll());
    }

    public DepartmentSwitchReport findById(long id) {
        return (DepartmentSwitchReport) this.departmentSwitchReportRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Chuyển đổi thiết bị", "id", Long.valueOf(id))));
    }

    public DepartmentSwitchReport findDepartmentSwitchReportWithDetails(long id) {
        return (DepartmentSwitchReport) this.departmentSwitchReportRepository.findDepartmentSwitchReportWithDetailsById(id)
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Chuyển đổi thiết bị", "id", Long.valueOf(id))));
    }

    @Transactional
    public DepartmentSwitchReport findDepartmentSwitchReportWithDetailsAndFiles(long id) {
        DepartmentSwitchReport departmentSwitchReport = findById(id);
        departmentSwitchReport.setDepartmentSwitchReportDetailList(findDepartmentSwitchReportWithDetails(id).getDepartmentSwitchReportDetailList());
        departmentSwitchReport.setDepartmentSwitchReportFileList(findDepartmentSwitchReportWithFiles(id).getDepartmentSwitchReportFileList());
        return departmentSwitchReport;
    }


    public DepartmentSwitchReport findDepartmentSwitchReportWithFiles(long id) {
        return (DepartmentSwitchReport) this.departmentSwitchReportRepository.findDepartmentSwitchReportWithFilesById(id)
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Chuyển đổi thiết bị", "id", Long.valueOf(id))));
    }

    public DepartmentSwitchReportSavingDto getSavingDto(long id) {
        DepartmentSwitchReport departmentSwitchReport = findDepartmentSwitchReportWithDetails(id);
        return this.mapstructMapper.departmentSwitchReportToDepartmentSwitchReportSavingDto(departmentSwitchReport);
    }

    public DepartmentSwitchReportExDto getExDto(long id) {
        DepartmentSwitchReport departmentSwitchReport = findDepartmentSwitchReportWithDetailsAndFiles(id);
        return this.mapstructMapper.departmentSwitchReportToDepartmentSwitchReportExDto(departmentSwitchReport);
    }

    @Transactional
    public void flushReport(long id) {
        DepartmentSwitchReport departmentSwitchReport = findDepartmentSwitchReportWithDetails(id);
        if (departmentSwitchReport.isFlush())
            throw new AppException("Biên bản này đã được duyệt!");
        Department departmentFrom = departmentSwitchReport.getDepartmentFrom();
        Department departmentTo = departmentSwitchReport.getDepartmentTo();
        departmentSwitchReport.getDepartmentSwitchReportDetailList()
                .stream().forEach(departmentSwitchReportDetail -> {
            Equipment equipment = departmentSwitchReportDetail.getEquipment();

            equipment.setDepartment(departmentTo);

            this.equipmentRepository.save(equipment);
            this.equipmentHistoryService.createEquipmentHistory(equipment, departmentFrom, departmentTo, departmentSwitchReport.getMessage());
        });
        departmentSwitchReport.setFlush(true);
        this.departmentSwitchReportRepository.save(departmentSwitchReport);
    }

    public void delete(long id) {
        DepartmentSwitchReport departmentSwitchReport = findById(id);
        if (departmentSwitchReport.isFlush())
            throw new AppException("Biên bản này đã duyệt không thể xóa!");
        try {
            this.departmentSwitchReportRepository.delete(departmentSwitchReport);
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Không thể xóa do có dữ liệu liên kết", e.getCause());
        }
    }

    public List<DepartmentSwitchReportDto> search(long departmentFromId, long departmentToId) {
        return (List<DepartmentSwitchReportDto>) this.departmentSwitchReportRepository.findAll(DepartmentSwitchReportSpecification.filterSearch(Long.valueOf(departmentFromId), Long.valueOf(departmentToId))).stream()
                .map(departmentSwitchReport -> this.mapstructMapper.departmentSwitchReportToDepartmentSwitchReportDto(departmentSwitchReport))
                .collect(Collectors.toList());
    }
}

