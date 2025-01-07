package com.vupt.SHM.mapstruct.mapper;

import com.vupt.SHM.dto.*;
import com.vupt.SHM.entity.*;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MapstructMapper {
    @Named("basicMappingAccount")
    AccountDto accountToAccountDto(Account paramAccount);

    @IterableMapping(qualifiedByName = {"basicMappingAccount"})
    List<AccountDto> accountsToAccountDtos(List<Account> paramList);

    AccountExDto accountToAccountExDto(Account paramAccount);

    Account accountDtoToAccount(AccountDto paramAccountDto);

    void accountDtoToSelectedAccount(AccountDto paramAccountDto, @MappingTarget Account paramAccount);

    AccountPasswordChangingDto accountDtoToAccountPasswordChangingDto(AccountDto paramAccountDto);

    DepartmentDto departmentToDepartmentDto(Department paramDepartment);

    Department departmentDtoToDepartment(DepartmentDto paramDepartmentDto);

    Department departmentDtoToSelectedDepartment(DepartmentDto paramDepartmentDto, @MappingTarget Department paramDepartment);

    List<DepartmentDto> departmentsToDepartmentDtos(List<Department> paramList);

    @Mapping(source = "department", target = "departmentDto")
    EmployeeDto employeeToEmployeeDto(Employee paramEmployee);

    @Mapping(source = "departmentDto", target = "department")
    Employee employeeDtoToEmployee(EmployeeDto paramEmployeeDto);

    @Mapping(source = "departmentDto", target = "department")
    Employee employeeDtoToSelectedEmployee(EmployeeDto paramEmployeeDto, @MappingTarget Employee paramEmployee);

    List<EmployeeDto> employeesToEmployeeDtos(List<Employee> paramList);

    RepairterDTO repairterToRepairterDTO(Repairter paramRepairter);

    Repairter repairterDTOToRepairter(RepairterDTO paramRepairterDTO);

    List<RepairterDTO> repairtersToRepairterDTOs(List<Repairter> paramList);

    CategoryDto categoryToCategoryDto(Category paramCategory);

    Category categoryDtoToCategory(CategoryDto paramCategoryDto);

    Category categoryDtoToSelectedCategory(CategoryDto paramCategoryDto, @MappingTarget Category paramCategory);

    List<CategoryDto> categoríesToCategoryDtos(List<Category> paramList);

    EquipmentDto equipmentToEquipmentDto(Equipment paramEquipment);

    Equipment equipmentDtoToEquipment(EquipmentDto paramEquipmentDto);

    Equipment equipmentDtoToSelectedEquipment(EquipmentDto paramEquipmentDto, @MappingTarget Equipment paramEquipment);

    List<EquipmentDto> equipmentsToEquipmentDtos(List<Equipment> paramList);

   // @Mapping(source = "departmentDto",target = "department")
    Equipment equipmentSavingDtoToEquipment(EquipmentSavingDto paramEquipmentSavingDto);

    void equipmentSavingDtoToSelectedEquipment(EquipmentSavingDto paramEquipmentSavingDto, @MappingTarget Equipment paramEquipment);

    EquipmentSavingDto equipmentDtoToEquipmentSavingdto(EquipmentDto paramEquipmentDto);

    @Mapping(source = "equipment", target = "equipmentDto")
    DepartmentSwitchReportDetailDto departmentSwitchReportDetailToDepartmentSwitchReportDetailDto(DepartmentSwitchReportDetail paramDepartmentSwitchReportDetail);

    @Mapping(source = "equipmentDto", target = "equipment")
    DepartmentSwitchReportDetail departmentSwitchReportDetailDtoToDepartmentSwitchReportDetail(DepartmentSwitchReportDetailDto paramDepartmentSwitchReportDetailDto);

    @Named("basicMapping")
    DepartmentSwitchReportDto departmentSwitchReportToDepartmentSwitchReportDto(DepartmentSwitchReport paramDepartmentSwitchReport);

    @IterableMapping(qualifiedByName = {"basicMapping"})
    List<DepartmentSwitchReportDto> departmentSwitchReportsToDepartmentSwitchReportDtos(List<DepartmentSwitchReport> paramList);

    @Named("extendedMapping")
    @Mapping(target = "departmentSwitchReportDetailList", source = "departmentSwitchReportDetailList")
    DepartmentSwitchReportSavingDto departmentSwitchReportToDepartmentSwitchReportSavingDto(DepartmentSwitchReport paramDepartmentSwitchReport);

    @Named("extendedMapping")
    @Mappings({@Mapping(target = "departmentSwitchReportDetailList", source = "departmentSwitchReportDetailList"), @Mapping(target = "departmentSwitchReportFileList", source = "departmentSwitchReportFileList")})
    DepartmentSwitchReportExDto departmentSwitchReportToDepartmentSwitchReportExDto(DepartmentSwitchReport paramDepartmentSwitchReport);

    @Mapping(target = "departmentSwitchReportDetailList", source = "departmentSwitchReportDetailList")
    DepartmentSwitchReport departmentSwitchReportExDtoToDepartmentSwitchReport(DepartmentSwitchReportSavingDto paramDepartmentSwitchReportSavingDto);

    void departmentSwitchReportExDtoToSelectedDepartmentSwitchReport(DepartmentSwitchReportSavingDto paramDepartmentSwitchReportSavingDto, @MappingTarget DepartmentSwitchReport paramDepartmentSwitchReport);

    DepartmentSwitchReportFile departmentSwitchReportFileDtoToDepartmentSwitchReportFile(DepartmentSwitchReportFileDto paramDepartmentSwitchReportFileDto);

    DepartmentSwitchReportFileDto departmentSwitchReportFileToDepartmentSwitchReportFileDto(DepartmentSwitchReportFile paramDepartmentSwitchReportFile);

    DepartmentSwitchReportAttachingDto departmentSwitchReportToDepartmentSwitchReportAttachingDto(DepartmentSwitchReport paramDepartmentSwitchReport);

    @Mapping(source = "department", target = "departmentDto")
    EquipmentRequestDto equipmentRequestToEquipmentRequestDto(EquipmentRequest equipmentRequest);
    List<EquipmentRequestDto> equipmentRequestsToEquipmentRequestDtos(List<EquipmentRequest> equipmentRequestList);
    @Mapping(source = "departmentDto",target = "department")
    EquipmentRequest equipmentRequestSavingDtoToEquipmentRequest(EquipmentRequestSavingDto equipmentRequestSavingDto);
    @Mapping(source = "departmentDto",target = "department")
    void equipmentRequestSavingDtoToSelectedEquipmentRequest(EquipmentRequestSavingDto equipmentRequestSavingDto,@MappingTarget EquipmentRequest equipmentRequest);
    EquipmentRequestSavingDto equipmentRequestDtoToEquipmentRequestSavingDto(EquipmentRequestDto equipmentRequestDto);

    EquipmentPackageDto equipmentPackageToEquipmentPackageDto(EquipmentPackage equipmentPackage);

    List<EquipmentPackageDto> equipmentPackagesToEquipmentPackageDtos(List<EquipmentPackage> equipmentPackages);

    EquipmentPackage equipmentPackageDtoToEquipmentPackage(EquipmentPackageDto equipmentPackageDto);

    void equipmentPackageDtoToSelectedEquipmentPackage(EquipmentPackageDto equipmentPackageDto,@MappingTarget EquipmentPackage equipmentPackage);
}


