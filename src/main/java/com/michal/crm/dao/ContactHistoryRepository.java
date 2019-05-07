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
    List<ContactHistory> findContactHistoriesByContact(@Param("contact")Contacts contact);
    List<ContactHistory> findContactHistoriesByContactId(@Param("contId")int contId);

//    @Query("select C from ContactHistory C where C.contact=:cont order by ")
}
