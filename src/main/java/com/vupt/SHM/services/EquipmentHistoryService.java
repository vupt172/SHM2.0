package com.vupt.SHM.services;

import com.vupt.SHM.entity.Department;
import com.vupt.SHM.entity.Equipment;
import com.vupt.SHM.entity.EquipmentHistory;
import com.vupt.SHM.repositories.EquipmentHistoryRepository;
import com.vupt.SHM.specifications.EquipmentHistorySpecification;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EquipmentHistoryService {
    @Autowired
    EquipmentHistoryRepository equipmentHistoryRepo;

    public List<EquipmentHistory> findAll() {
        return this.equipmentHistoryRepo.findAll();
    }

    public void createEquipmentHistory(Equipment equipment, Department fromDepartment, Department toDepartment, String message) {
        EquipmentHistory newEquipmentHistory = new EquipmentHistory();
        newEquipmentHistory.setFromDepartment(fromDepartment);
        newEquipmentHistory.setToDepartment(toDepartment);
        newEquipmentHistory.setEquipment(equipment);
        newEquipmentHistory.setMessage(message);
        newEquipmentHistory.setTransportDate(Calendar.getInstance().getTime());
        this.equipmentHistoryRepo.save(newEquipmentHistory);
    }

    public List<EquipmentHistory> search(String code, long departmentFromId, long departmentToId) {
        return this.equipmentHistoryRepo.findAll(EquipmentHistorySpecification.filterSearch(code, Long.valueOf(departmentFromId), Long.valueOf(departmentToId)));
    }
}


