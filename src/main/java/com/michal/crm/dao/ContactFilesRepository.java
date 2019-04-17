package com.michal.crm.dao;

import com.michal.crm.model.summaries.ContactFiles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ContactFilesRepository extends CrudRepository<ContactFiles, Integer>{
}
