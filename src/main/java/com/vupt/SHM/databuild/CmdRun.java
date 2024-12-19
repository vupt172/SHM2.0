package com.vupt.SHM.databuild;

import com.vupt.SHM.repositories.AccountRepository;
import com.vupt.SHM.repositories.DepartmentSwitchReportFileRepository;
import com.vupt.SHM.repositories.EquipmentHistoryRepository;
import com.vupt.SHM.repositories.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;


public class CmdRun {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    EquipmentHistoryRepository equipmentHistoryRepository;
    @Autowired
    DepartmentSwitchReportFileRepository departmentSwitchReportFileRepository;

    @Bean
    public CommandLineRunner getCommandLineRunner() {
        return args -> {

        };
    }
}

