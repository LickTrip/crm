package com.michal.crm.dao;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Tasks;
import com.michal.crm.model.Users;
import com.michal.crm.model.summaries.TaskContacts;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TaskContactsRepository extends CrudRepository<TaskContacts, Integer>{
    List<TaskContacts> findTaskContactsByTaskIdAndUser(@Param("id") int taskID, @Param("user") Users user);

    @Query("select T from TaskContacts T where T.contact=:cont and T.task=:task and T.user = :user")
    TaskContacts getTaskContact(@Param("cont")Contacts cont, @Param("task")Tasks task, @Param("user") Users user);

    @Query("select T from TaskContacts T where T.contact=:cont and T.user=:user")
    List<TaskContacts> getAllContactsTasks(@Param("cont") Contacts cont, @Param("user") Users user);
}
