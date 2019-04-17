package com.michal.crm.dao;

import com.michal.crm.model.Addresses;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AddressesRepository extends CrudRepository<Addresses, Integer>{
}
