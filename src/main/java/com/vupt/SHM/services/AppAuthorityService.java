package com.vupt.SHM.services;

import com.vupt.SHM.entity.AppAuthority;
import com.vupt.SHM.repositories.AppAuthorityRepository;
import com.vupt.SHM.specifications.AppAuthoritySpecification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppAuthorityService {
    @Autowired
    AppAuthorityRepository appAuthorityRepo;

    public void save(AppAuthority appAuthority) {
        this.appAuthorityRepo.save(appAuthority);
    }

    public List<AppAuthority> findAll() {
        return this.appAuthorityRepo.findAll();
    }

    public List<AppAuthority> findByAppFunction(long appFunctionId) {
        return this.appAuthorityRepo.findByAppFunction_Id(appFunctionId);
    }

    public List<AppAuthority> search(long appFunctionId) {
        return this.appAuthorityRepo.findAll(AppAuthoritySpecification.filterSearch(Long.valueOf(appFunctionId)));
    }
}
