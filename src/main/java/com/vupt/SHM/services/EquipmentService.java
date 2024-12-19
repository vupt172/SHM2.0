package com.vupt.SHM.services;

import com.vupt.SHM.constant.EquipmentStatus;
import com.vupt.SHM.dto.DepartmentSwitchDto;
import com.vupt.SHM.dto.EquipmentDto;
import com.vupt.SHM.dto.EquipmentSavingDto;
import com.vupt.SHM.entity.Category;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.Equipment;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.EquipmentRepository;
import com.vupt.SHM.services.CategoryService;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.services.EmployeeService;
import com.vupt.SHM.services.EquipmentHistoryService;
import com.vupt.SHM.specifications.EquipmentSpecification;
import com.vupt.SHM.utils.DisplayTextUtils;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class EquipmentService {
    @Autowired
    EquipmentRepository equipmentRepo;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DepartmentService departmentService;

    public List<EquipmentDto> findAll() {
        return this.mapstructMapper.equipmentsToEquipmentDtos(this.equipmentRepo.findAll());
    }

    @Autowired
    EmployeeService employeeService;
    @Autowired
    MapstructMapper mapstructMapper;
    @Autowired
    EquipmentHistoryService equipmentHistoryService;

    public List<EquipmentDto> findByDepartmentId(long departmentId) {
        return (List<EquipmentDto>) this.equipmentRepo.findByDepartment_Id(departmentId).stream()
                .map(equipment -> this.mapstructMapper.equipmentToEquipmentDto(equipment))
                .collect(Collectors.toList());
    }

    @Transactional
    public void save(EquipmentSavingDto equipmentSavingDto) {
        if (equipmentSavingDto.getId() == 0L) {
            Equipment newEquipment = this.mapstructMapper.equipmentSavingDtoToEquipment(equipmentSavingDto);
            Category category = this.categoryService.findById(equipmentSavingDto.getCategory().getId());
            newEquipment.setCategory(category);
            Department department = this.departmentService.findById(equipmentSavingDto.getDepartment().getId());
            newEquipment.setDepartment(department);
            this.equipmentRepo.save(newEquipment);
            newEquipment.setCode(newEquipment.generateCode());
            this.equipmentRepo.save(newEquipment);

            this.equipmentHistoryService.createEquipmentHistory(newEquipment, null, newEquipment.getDepartment(), "Tạo mới");
        } else {
            Equipment curEquipment = findById(equipmentSavingDto.getId());

            this.mapstructMapper.equipmentSavingDtoToSelectedEquipment(equipmentSavingDto, curEquipment);
            Category category = this.categoryService.findById(equipmentSavingDto.getCategory().getId());
            Department department = this.departmentService.findById(equipmentSavingDto.getDepartment().getId());
            curEquipment.setCategory(category);
            curEquipment.setDepartment(department);

            this.equipmentRepo.save(curEquipment);
        }
    }

    public void generateCode(EquipmentDto equipmentDto) {
        Equipment curEquipment = findById(equipmentDto.getId());
        curEquipment.generateCode();
        this.equipmentRepo.save(curEquipment);
    }

    public void switchDepartment(DepartmentSwitchDto departmentSwitchDTO) {
        Equipment curEquipment = findById(departmentSwitchDTO.getEquipmentDTO().getId());
        Department curDepartment = curEquipment.getDepartment();
        Department newDepartment = this.departmentService.findById(departmentSwitchDTO.getNewDepartment().getId());
        curEquipment.setDepartment(newDepartment);
        this.equipmentRepo.save(curEquipment);
    }


    public Equipment findById(long id) {
        return (Equipment) this.equipmentRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Thiết bị", "id", Long.valueOf(id))));
    }

    public void delete(long id) {
        try {
            Equipment equipment = findById(id);
            this.equipmentRepo.delete(equipment);
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Không thể xóa do có dữ liệu liên kết", e.getCause());
        }
    }

    public List<EquipmentDto> search(String name, String code, EquipmentStatus status, long categoryId, long departmentId) {
        return (List<EquipmentDto>) this.equipmentRepo.findAll(EquipmentSpecification.filterSearch(name, code, status, Long.valueOf(categoryId), Long.valueOf(departmentId))).stream()
                .map(equipment -> this.mapstructMapper.equipmentToEquipmentDto(equipment))
                .collect(Collectors.toList());
    }

    public long count(long categoryId) {
        return this.equipmentRepo.countByCategory_Id(categoryId);
    }

    public long count(long categoryId, long departmentId) {
        return this.equipmentRepo.countByCategory_IdAndDepartmentId(categoryId, departmentId);
    }
}
