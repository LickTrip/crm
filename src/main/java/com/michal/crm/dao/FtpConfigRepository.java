package com.michal.crm.dao;

import com.michal.crm.model.UserFtpConfig;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FtpConfigRepository extends CrudRepository<UserFtpConfig, Integer> {
}
