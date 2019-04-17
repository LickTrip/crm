package com.michal.crm.dao;

import com.michal.crm.model.UserEmailConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EmailConfigRepository extends CrudRepository<UserEmailConfig, Integer> {
}
