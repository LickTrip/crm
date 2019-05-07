package com.michal.crm.dao;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ContactsRepository extends CrudRepository<Contacts, Integer>{
    Contacts findContactsByIdAndUser(@Param("id") int id, @Param("user") Users user);

    @Query("select C from Contacts C where (LOWER(C.surname) = LOWER(:name) or LOWER(C.firstName) = LOWER(:name)) and C.user = :user order by C.id desc")
    List<Contacts> getContactsByName(@Param("name") String name, @Param("user") Users user);
}
