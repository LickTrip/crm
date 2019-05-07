package com.michal.crm.dao;

import com.michal.crm.model.ContactHistory;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ContactHistoryRepository extends CrudRepository<ContactHistory, Integer> {
    List<ContactHistory> findContactHistoriesByUserOrderByCreateDateDesc(@Param("user")Users user);
    List<ContactHistory> findContactHistoriesByContactAndUser(@Param("contact")Contacts contact, @Param("user")Users user);
    List<ContactHistory> findContactHistoriesByContactId(@Param("contId")int contId);
}
