package com.michal.crm.dao;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Files;
import com.michal.crm.model.Users;
import com.michal.crm.model.summaries.ContactFiles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ContactFilesRepository extends CrudRepository<ContactFiles, Integer> {
    List<ContactFiles> findContactFilesByContactAndUser(@Param("contact") Contacts contact, @Param("user") Users user);

    List<ContactFiles> findContactFilesByFileAndUser(@Param("file") Files file, @Param("user") Users user);

    ContactFiles findContactFilesByFileAndContactAndUser(@Param("file") Files file, @Param("contact") Contacts contact, @Param("user") Users user);
}
