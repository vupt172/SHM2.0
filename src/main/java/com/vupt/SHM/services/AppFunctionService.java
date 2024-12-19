package com.vupt.SHM.services;

import com.vupt.SHM.entity.AppFunction;
import com.vupt.SHM.repositories.AppFunctionRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppFunctionService {
    @Autowired
    AppFunctionRepository appFunctionRepository;

    public List<AppFunction> findAll() {
        return this.appFunctionRepository.findAll();
    }
}

