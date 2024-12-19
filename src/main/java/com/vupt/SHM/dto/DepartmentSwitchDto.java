package com.vupt.SHM.dto;

import lombok.Data;

@Data
 public class DepartmentSwitchDto {
   private EquipmentDto equipmentDTO;
   private DepartmentDto newDepartment;
   private String message;

}

