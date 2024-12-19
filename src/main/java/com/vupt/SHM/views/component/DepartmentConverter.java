package com.vupt.SHM.views.component;

import com.vupt.SHM.dto.DepartmentDto;
import com.vupt.SHM.services.DepartmentService;
import javafx.util.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DepartmentConverter
        extends StringConverter<DepartmentDto> {
    @Autowired
    private DepartmentService departmentService;

    public String toString(DepartmentDto departmentDto) {
        return (departmentDto == null) ? "" : departmentDto.getName();
    }

    public DepartmentDto fromString(String string) {
        return this.departmentService.getDTO(string);
    }
}
