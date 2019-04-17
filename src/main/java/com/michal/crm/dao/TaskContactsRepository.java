package com.michal.crm.dao;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Tasks;
import com.michal.crm.model.summaries.TaskContacts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TaskContactsRepository extends CrudRepository<TaskContacts, Integer>{
    List<TaskContacts> findTaskContactsByTaskId(@Param("id") int taskID);

    @Query("select T from TaskContacts T where T.contact=:cont and T.task=:task")
    TaskContacts getTaskContact(@Param("cont")Contacts cont, @Param("task")Tasks task);
}
