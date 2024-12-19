package com.vupt.SHM.services;

import com.vupt.SHM.constant.DepartmentType;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.DepartmentRepository;
import com.vupt.SHM.specifications.DepartmentSpecification;
import com.vupt.SHM.utils.DisplayTextUtils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepo;
    @Autowired
    private MapstructMapper mapstructMapper;

    public List<Department> findAll() {
        return this.departmentRepo.findAll();
    }

    public List<DepartmentDto> findAllDto() {
        return this.mapstructMapper.departmentsToDepartmentDtos(this.departmentRepo.findAll());
    }

    public List<DepartmentDto> findAllDtoIgnoreSuspended() {
        List<DepartmentDto> departmentDtoIgnoreSuspendedList = (List<DepartmentDto>) findAllDto().stream().filter(departmentDto -> !departmentDto.isSuspended()).collect(Collectors.toList());
        return departmentDtoIgnoreSuspendedList;
    }

    public List<DepartmentDto> search(String name, DepartmentType type) {
        return (List<DepartmentDto>) this.departmentRepo.findAll(DepartmentSpecification.containsNameAndTypeIfPresent(name, type)).stream()
                .map(department -> this.mapstructMapper.departmentToDepartmentDto(department))
                .collect(Collectors.toList());
    }

    public void save(DepartmentDto departmentDTO) {
        if (departmentDTO.getId() == 0L) {
            checkIfExistsByName(departmentDTO.getName());
            checkIfExistsByCode(departmentDTO.getCode());
            Department newDepartment = this.mapstructMapper.departmentDtoToDepartment(departmentDTO);
            if (departmentDTO.getParent() == null) {
                newDepartment.setParent(null);
            } else {
                Department parent = findById(departmentDTO.getParent().getId());
                newDepartment.setParent(parent);
            }
            this.departmentRepo.save(newDepartment);
        } else {
            Department curDepartment = this.departmentRepo.findById(Long.valueOf(departmentDTO.getId())).get();
            if (!curDepartment.getName().equalsIgnoreCase(departmentDTO.getName()))
                checkIfExistsByName(departmentDTO.getName());
            if (!curDepartment.getCode().equalsIgnoreCase(departmentDTO.getCode()))
                checkIfExistsByCode(departmentDTO.getCode());
            this.mapstructMapper.departmentDtoToSelectedDepartment(departmentDTO, curDepartment);
            if (departmentDTO.getParent() == null) {
                curDepartment.setParent(null);
            } else {
                Department parent = findById(departmentDTO.getParent().getId());
                curDepartment.setParent(parent);
            }
            this.departmentRepo.save(curDepartment);
        }
    }

    public Department findById(long id) {
        return (Department) this.departmentRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Bộ phận", "id", Long.valueOf(id))));
    }

    public Department findByName(String name) {
        return (Department) this.departmentRepo.findByName(name)
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Bộ phận", "name", name)));
    }

    public List<DepartmentDto> getChildDepartmentsByIdIgnoreSuspended(long id) {
        return (List<DepartmentDto>) this.departmentRepo.findByParent_id(id).stream()
                .filter(department -> !department.isSuspended())
                .map(department -> this.mapstructMapper.departmentToDepartmentDto(department))
                .collect(Collectors.toList());
    }

    public DepartmentDto getDTO(long id) {
        Department d = findById(id);
        return this.mapstructMapper.departmentToDepartmentDto(d);
    }

    public DepartmentDto getDTO(String name) {
        Department d = findByName(name);
        return this.mapstructMapper.departmentToDepartmentDto(d);
    }

    public void checkIfExistsByName(String name) {
        if (this.departmentRepo.existsByName(name))
            throw new AppException(DisplayTextUtils.getIsExistByMessage("Bộ phận", "tên", name));
    }

    public void checkIfExistsByCode(String code) {
        if (code.trim().equals(""))
            return;
        if (this.departmentRepo.existsByCode(code))
            throw new AppException(DisplayTextUtils.getIsExistByMessage("Bộ phận", "code", code));
    }

    public void delete(long id) {
        try {
            Department department = findById(id);
            this.departmentRepo.delete(department);
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Không thể xóa do có dữ liệu liên kết", e.getCause());
        }
    }
}
