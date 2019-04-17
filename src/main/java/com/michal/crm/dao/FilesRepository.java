package com.michal.crm.dao;

import com.michal.crm.model.Files;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface FilesRepository extends CrudRepository<Files, Integer>{
}
