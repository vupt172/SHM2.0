package com.vupt.SHM.services;

import com.vupt.SHM.entity.Role;
import com.vupt.SHM.exceptions.AppException;
import com.vupt.SHM.repositories.RoleRepository;
import com.vupt.SHM.specifications.RoleSpecification;
import com.vupt.SHM.utils.DisplayTextUtils;

import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class RoleService {
    @Autowired
    RoleRepository roleRepo;

    public List<Role> findAll() {
        return this.roleRepo.findAll();
    }

    public void save(Role role) {
        this.roleRepo.save(role);
    }

    public Role findById(long id) {
        return (Role) this.roleRepo.findById(id).orElseThrow(() -> new AppException(DisplayTextUtils.getNotFoundMessage("Vai trò", "id", Long.valueOf(id))));
    }

    public List<Role> search(String code) {
        return this.roleRepo.findAll(RoleSpecification.filterSearch(code));
    }
}

