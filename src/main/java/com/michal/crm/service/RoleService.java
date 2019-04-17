package com.michal.crm.service;

import com.michal.crm.dao.RoleRepository;
import com.michal.crm.model.Role;
import com.michal.crm.model.types.RoleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public void saveRike(List<Role> roleList){
        roleRepository.saveAll(roleList);
    }

    public List<Role> getAllRole(){
        return (List<Role>) roleRepository.findAll();
    }

    public Role getSpecificRole(RoleTypes roleType){
        return roleRepository.findByName(roleType);
    }
}
