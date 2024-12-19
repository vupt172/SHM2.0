package com.vupt.SHM.services;

import com.vupt.SHM.dto.EmployeeDto;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.Employee;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.EmployeeRepository;
import com.vupt.SHM.services.DepartmentService;
import com.vupt.SHM.specifications.EmployeeSpecification;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepo;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    MapstructMapper mapstructMapper;

    public List<EmployeeDto> findALl() {
        return this.mapstructMapper.employeesToEmployeeDtos(this.employeeRepo.findAll());
    }

    public List<EmployeeDto> findByIsManager() {
        return (List<EmployeeDto>) this.employeeRepo.findByIsManager(Boolean.valueOf(true)).stream()
                .map(employee -> this.mapstructMapper.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
    }

    public void save(EmployeeDto employeeDTO) {
        if (employeeDTO.getId() == 0L) {
            checkIfExistsByName(employeeDTO.getFullName());
            checkIfExistsByUsername(employeeDTO.getUsername());
            Employee newEmployee = this.mapstructMapper.employeeDtoToEmployee(employeeDTO);
            Department department = this.departmentService.findById(employeeDTO.getDepartmentDto().getId());
            newEmployee.setDepartment(department);
            this.employeeRepo.save(newEmployee);
        } else {
            Employee curEmployee = findById(employeeDTO.getId());
            if (!curEmployee.getFullName().equals(employeeDTO.getFullName())) {
                checkIfExistsByName(employeeDTO.getFullName());
            }
            if (curEmployee.getUsername() != null && !curEmployee.getUsername().equals(employeeDTO.getUsername())) {
                checkIfExistsByUsername(employeeDTO.getUsername());
            }
            this.mapstructMapper.employeeDtoToSelectedEmployee(employeeDTO, curEmployee);
            Department department = this.departmentService.findById(employeeDTO.getDepartmentDto().getId());
            curEmployee.setDepartment(department);
            this.employeeRepo.save(curEmployee);
        }
    }

    public void delete(long id) {
        try {
            Employee employee = findById(id);
            this.employeeRepo.delete(employee);
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Không thể xóa do có dữ liệu liên kết", e.getCause());
        }
    }

    public Employee findById(long id) {
        return (Employee) this.employeeRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new AppException("Không tìm thấy employee với id là " + id));
    }

    public EmployeeDto getDTO(long id) {
        Employee e = findById(id);
        return this.mapstructMapper.employeeToEmployeeDto(e);
    }

    public void checkIfExistsByName(String name) {
        if (this.employeeRepo.existsByFullName(name))
            throw new AppException("Nhân viên đã tồn tại với tên là " + name);
    }

    public void checkIfExistsByUsername(String username) {
        if (this.employeeRepo.existsByUsername(username))
            throw new AppException("Nhân viên đã tồn tại với username là " + username);
    }

    public List<EmployeeDto> search(String name, long departmentId) {
        return (List<EmployeeDto>) this.employeeRepo.findAll(EmployeeSpecification.filterSearch(name, Long.valueOf(departmentId))).stream()
                .map(employee -> this.mapstructMapper.employeeToEmployeeDto(employee))
                .collect(Collectors.toList());
    }
}
