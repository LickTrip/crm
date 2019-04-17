package com.michal.crm.dao;

import com.michal.crm.model.summaries.ContactNotes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ContactNotesRepository extends CrudRepository<ContactNotes, Integer>{
    List<ContactNotes> findContactNotesByContactId(@Param("id") int contId);
}
