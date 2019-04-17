package com.michal.crm.dao;

import com.michal.crm.model.Role;
import com.michal.crm.model.types.RoleTypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(@Param("name")RoleTypes roleType);
}
