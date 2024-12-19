package com.vupt.SHM.services;

import com.vupt.SHM.dto.RepairterDTO;
import com.vupt.SHM.entity.Repairter;
import com.vupt.SHM.mapstruct.mapper.MapstructMapper;
import com.vupt.SHM.repositories.RepairterRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RepairterService {
    @Autowired
    RepairterRepository repairterRepo;
    @Autowired
    MapstructMapper mapstructMapper;

    public List<RepairterDTO> findALl() {
        return this.mapstructMapper.repairtersToRepairterDTOs(this.repairterRepo.findAll());
    }

    public void save(RepairterDTO repairterDTO) {
        if (repairterDTO.getId() == 0L) {
            Repairter newRepairter = this.mapstructMapper.repairterDTOToRepairter(repairterDTO);
            this.repairterRepo.save(newRepairter);
        }
    }
}
