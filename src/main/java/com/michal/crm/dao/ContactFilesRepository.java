package com.michal.crm.dao;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.summaries.ContactFiles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ContactFilesRepository extends CrudRepository<ContactFiles, Integer>{
    List<ContactFiles> findContactFilesByContact(@Param("contact")Contacts contact);
}
