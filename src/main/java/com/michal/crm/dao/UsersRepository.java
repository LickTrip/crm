package com.michal.crm.dao;

import com.michal.crm.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UsersRepository extends CrudRepository<Users, Integer>{
    Users findByUsername(@Param("username") String username);
}
