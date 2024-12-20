package com.vupt.SHM.services;

import com.vupt.SHM.constant.AppConstants;
import com.vupt.SHM.dto.EquipmentRequestDto;
import com.vupt.SHM.dto.EquipmentRequestSavingDto;
import com.vupt.SHM.entity.Category;
import com.vupt.SHM.entity.Equipment;
import com.vupt.SHM.entity.EquipmentRequest;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.exceptions.SQLException;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.EquipmentRequestRepository;
import com.vupt.SHM.specifications.EquipmentRequestSpecification;
import com.vupt.SHM.utils.DisplayTextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentRequestService {
    @Autowired
    MapstructMapper mapstructMapper;
    @Autowired
    EquipmentRequestRepository equipmentRequestRepository;

    public List<EquipmentRequest> findAll(){
        return equipmentRequestRepository.findAll(Sort.by(Sort.Order.desc("id")));
    }
    public List<EquipmentRequestDto> findAllDto(){
        return mapstructMapper.equipmentRequestsToEquipmentRequestDtos(findAll());
    }
    public List<EquipmentRequestDto> findByDepartmentId(long departmentId){
        return  equipmentRequestRepository.findByDepartment_Id(departmentId,Sort.by(Sort.Order.desc("id"))).stream()
                .map(equipmentRequest -> mapstructMapper.equipmentRequestToEquipmentRequestDto(equipmentRequest))
                .collect(Collectors.toList());
    }
    public EquipmentRequest findById(long id){
        return equipmentRequestRepository.findById(id).orElseThrow(()->new AppException(DisplayTextUtils.getNotFoundMessage(AppConstants.MENU_REQUIPMENT_REQUEST, "id", Long.valueOf(id))));
    }

    public List<EquipmentRequestDto> filterSearch(long departmentId, Date startDate, Date endDate){
        return equipmentRequestRepository.findAll(EquipmentRequestSpecification.filterSearch(departmentId,startDate,endDate),Sort.by(Sort.Order.desc("id")))
                .stream().map(equipmentRequest -> mapstructMapper.equipmentRequestToEquipmentRequestDto(equipmentRequest))
                .collect(Collectors.toList());
    }

    public void save(EquipmentRequestSavingDto equipmentRequestSavingDto){
        if(equipmentRequestSavingDto.getId()==0){
            EquipmentRequest equipmentRequest=mapstructMapper.equipmentRequestSavingDtoToEquipmentRequest(equipmentRequestSavingDto);
            equipmentRequestRepository.save(equipmentRequest);
        }
        else {
            EquipmentRequest equipmentRequest=findById(equipmentRequestSavingDto.getId());
            mapstructMapper.equipmentRequestSavingDtoToSelectedEquipmentRequest(equipmentRequestSavingDto,equipmentRequest);
            equipmentRequestRepository.save(equipmentRequest);
        }

    }
    public void delete(long id){
        try {
            EquipmentRequest equipmentRequest = findById(id);
            this.equipmentRequestRepository.delete(equipmentRequest);
        } catch (DataIntegrityViolationException e) {
            throw new SQLException("Không thể xóa do có dữ liệu liên kết", e.getCause());
        }
    }

    public void approveRequest(long id) {
        EquipmentRequest equipmentRequest=findById(id);
        equipmentRequest.setDone(true);
        equipmentRequestRepository.save(equipmentRequest);
    }
    public void unapproveRequest(long id){
        EquipmentRequest equipmentRequest=findById(id);
        equipmentRequest.setDone(false);
        equipmentRequestRepository.save(equipmentRequest);
    }
}
