package com.michal.crm.dao;

import com.michal.crm.model.Company;
import com.michal.crm.model.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CompanyRepository extends CrudRepository<Company, Integer> {
    Company findByIdAndUser(@Param("id") int id, @Param("user") Users user);

    @Query("select C from Company C where (LOWER(C.name) = LOWER(:name)) and C.user = :user order by C.id desc")
    List<Company> getCompanyByName(@Param("name") String name, @Param("user") Users user);

    List<Company> findTop10ByUser(@Param("user") Users user);
}
