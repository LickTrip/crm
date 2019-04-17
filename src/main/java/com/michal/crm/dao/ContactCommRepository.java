package com.michal.crm.dao;

import com.michal.crm.model.summaries.ContactComm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ContactCommRepository extends CrudRepository<ContactComm, Integer>{
}
