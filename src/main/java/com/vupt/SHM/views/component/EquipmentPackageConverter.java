package com.vupt.SHM.views.component;

import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.dto.EquipmentPackageDto;
import com.vupt.SHM.entity.EquipmentPackage;
import com.vupt.SHM.services.EquipmentPackageService;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EquipmentPackageConverter extends StringConverter<EquipmentPackageDto> {
    @Autowired
    private EquipmentPackageService equipmentPackageService;

    public String toString(EquipmentPackageDto equipmentPackageDto) {
        return (equipmentPackageDto == null) ? "" : equipmentPackageDto.getName();
    }

    public EquipmentPackageDto fromString(String string) {
        return this.equipmentPackageService.parseDto(string);
    }
}
