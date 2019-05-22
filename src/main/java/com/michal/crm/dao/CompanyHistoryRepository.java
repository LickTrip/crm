package com.michal.crm.dao;

import com.michal.crm.model.Company;
import com.michal.crm.model.CompanyHistory;
import com.michal.crm.model.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CompanyHistoryRepository extends CrudRepository<CompanyHistory, Integer> {
    List<CompanyHistory> findCompanyHistoriesByUserOrderByCreateDateDesc(@Param("user")Users user);
    List<CompanyHistory> findCompanyHistoriesByCompanyAndUser(@Param("company")Company company, @Param("user") Users user);
}
