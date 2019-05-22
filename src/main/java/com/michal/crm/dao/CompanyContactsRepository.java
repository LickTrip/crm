package com.michal.crm.dao;

import com.michal.crm.model.Company;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Users;
import com.michal.crm.model.summaries.CompanyContacts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CompanyContactsRepository extends CrudRepository<CompanyContacts, Integer> {
    List<CompanyContacts> findCompanyContactsByCompanyAndUser(@Param("company")Company company, @Param("user")Users user);
    List<CompanyContacts> findCompanyContactsByContactAndCompanyAndUser(@Param("contact")Contacts contact ,@Param("company")Company company, @Param("user")Users user);
}
