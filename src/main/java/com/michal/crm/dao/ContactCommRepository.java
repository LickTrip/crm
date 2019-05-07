package com.michal.crm.dao;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Users;
import com.michal.crm.model.summaries.ContactComm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ContactCommRepository extends CrudRepository<ContactComm, Integer>{
    List<ContactComm> findContactCommsByContactAndUser(@Param("contact")Contacts contacts, @Param("user")Users user);
}
