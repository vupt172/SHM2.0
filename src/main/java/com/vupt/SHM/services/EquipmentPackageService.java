package com.vupt.SHM.services;

import com.vupt.SHM.constant.AppConstants;
import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.EquipmentPackageDto;
import com.vupt.SHM.entity.Category;
import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.EquipmentPackage;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.EquipmentPackageRepository;
import com.vupt.SHM.utils.DisplayTextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.WeakHashMap;

@Service
public class EquipmentPackageService {
    @Autowired
    EquipmentPackageRepository equipmentPackageRepo;
    @Autowired
    MapstructMapper mapstructMapper;

    public List<EquipmentPackage> findAll() {
        return equipmentPackageRepo.findAll();
    }

    public List<EquipmentPackageDto> findAllDto() {
        return mapstructMapper.equipmentPackagesToEquipmentPackageDtos(findAll());
    }

    public EquipmentPackage findById(long id) {
        return equipmentPackageRepo.findById(id)
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage(AppConstants.MENU_EQUIPMENT_PACKAGE, "id", Long.valueOf(id))));
    }
    public EquipmentPackage findByName(String name) {
        return equipmentPackageRepo.findByName(name)
                .orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage(AppConstants.MENU_EQUIPMENT_PACKAGE, "name", name)));
    }

    public void save(EquipmentPackageDto equipmentPackageDto) {
        if (equipmentPackageDto.getId() == 0L) {
            if (equipmentPackageRepo.existsByName(equipmentPackageDto.getName()))
                throw new AppException("Đối tượng đã tồn tại với tên là " + equipmentPackageDto.getName());
            EquipmentPackage equipmentPackage = this.mapstructMapper.equipmentPackageDtoToEquipmentPackage(equipmentPackageDto);
            this.equipmentPackageRepo.save(equipmentPackage);
        } else {
            EquipmentPackage curEquipmentPackage = findById(equipmentPackageDto.getId());
            if (!equipmentPackageDto.getName().equals(curEquipmentPackage.getName())) {
                if (equipmentPackageRepo.existsByName(equipmentPackageDto.getName()))
                    throw new AppException("Đối tượng đã tồn tại với tên là " + equipmentPackageDto.getName());
            }
            this.mapstructMapper.equipmentPackageDtoToSelectedEquipmentPackage(equipmentPackageDto, curEquipmentPackage);
            this.equipmentPackageRepo.save(curEquipmentPackage);
        }
    }

    public void delete(long id) {
        try {
            EquipmentPackage equipmentPackage = findById(id);
            equipmentPackageRepo.delete(equipmentPackage);
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Không thể xóa do có dữ liệu liên kết", e.getCause());
        }
    }

    public EquipmentPackageDto parseDto(String name) {
        EquipmentPackage equipmentPackage = findByName(name);
        return this.mapstructMapper.equipmentPackageToEquipmentPackageDto(equipmentPackage);
    }
}
